import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

export let options = {
    vus: 1,
    iterations: 1,
};

export let successSignup = new Counter('signup_success');
export let failSignup = new Counter('signup_fail');
export let successLogin = new Counter('login_success');
export let failLogin = new Counter('login_fail');
export let successCreateLecture = new Counter('lecture_create_success');
export let failCreateLecture = new Counter('lecture_create_fail');

export default function () {
    const baseUrls = ['http://localhost:8080'];
    const baseUrl = baseUrls[0];

    // 1. ADMIN 로그인
    const loginPayload = JSON.stringify({
        userNumber: 'asdf',
        password: '!1Password',
    });

    const loginRes = http.post(`${baseUrl}/auth/login`, loginPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    console.log(`[로그인 응답] status: ${loginRes.status}, body: ${loginRes.body}`);

    let token = null;
    try {
        const jsonBody = loginRes.json();
        token = jsonBody?.data?.accessTokken;  // ← 'accessTokken' 맞춤
        console.log(`[로그인 토큰] ${token}`);
    } catch (e) {
        console.log(`[JSON 파싱 실패]`, e);
    }

    // Authorization 헤더 없이 다음 요청하면 403 발생하니 예외 처리
    if (!token) {
        console.error(`토큰 추출 실패 - 강의 생성 중단`);
        return;
    }

    // 2. 강의 생성 요청
    const lecturePayload = JSON.stringify({
        name: '고급 알고리즘',
        code: 'CS401',
        maxEnrollment: 100,
        dayOfWeek: 'MON',
        startTime: '09:00:00',
        endTime: '10:30:00'
    });

    const res = http.post(`${baseUrl}/lectures`, lecturePayload, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        }
    });

    console.log(`[강의 생성 응답] status: ${res.status}, body: ${res.body}`);
}
