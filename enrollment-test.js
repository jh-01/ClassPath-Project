import { check, sleep } from 'k6';
import http from 'k6/http';
import { Counter } from 'k6/metrics';
import encoding from 'k6/encoding';

export let options = {
    vus: 50,
    iterations: 1000,
};

export let success200 = new Counter('enrollment_success');
export let success409 = new Counter('enrollment_conflict');
export let fail = new Counter('enrollment_fail');

export default function () {
    const baseUrl = 'http://localhost:8080';
    const idNumber = __VU;

    // 1. 로그인 (userNumber = 2023xxxx 형식)
    const userNumber = `2023${String(idNumber).padStart(4, '0')}`;
    const password = 'Test@1234';

    const loginPayload = JSON.stringify({ userNumber, password });

    const loginRes = http.post(`${baseUrl}/auth/login`, loginPayload, {
        headers: { 'Content-Type': 'application/json' },
    });
    console.log('loginRes body:', loginRes.body);

    let token = null;
    let userId = null;
    try {
        const json = loginRes.json();
        token = json?.data?.accessTokken;
        userId = json?.data?.user?.id;
    } catch (e) {
        console.error(`[로그인 JSON 파싱 실패]`, e);
    }

    if (!token) {
        console.error(`[로그인 실패] VU: ${idNumber}, status: ${loginRes.status}`);
        fail.add(1);
        return;
    }

    // 2. 수강 신청 요청
    const enrollPayload = JSON.stringify({
        userId: userId,
        lectureId: 1,
    });

    const enrollRes = http.post(`${baseUrl}/enrollment`, enrollPayload, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        }
    });

    if (enrollRes.status === 200) success200.add(1);
    else if (enrollRes.status === 409) success409.add(1);
    else {
        console.error(`[알 수 없는 응답 상태] userId=${userId}, status=${enrollRes.status}, body=${enrollRes.body}`);
        fail.add(1);
    }

    check(enrollRes, {
        'HTTP 상태 코드: 200 or 409': (r) => r.status === 200 || r.status === 409,
    });

    sleep(0.3);
}