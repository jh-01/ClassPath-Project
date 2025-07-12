import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 1,         // 동시 사용자 수
    iterations: 50,  // 총 50명 생성
};

export default function () {
    const id = (__VU - 1) * 1 + (__ITER + 1); // VU * 반복횟수 → 고유 ID

    const userNumber = `2023${String(id).padStart(4, '0')}`; // 20230001 ~
    const payload = JSON.stringify({
        name: `user${id}`,
        userNumber: userNumber,
        password: "Test@1234",
        role: "STUDENT"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post('http://localhost:8080/users/register', payload, params);

    const success = check(res, {
        '회원가입 성공 (201)': (r) => r.status === 201,
    });

    if (!success) {
        console.error(`회원가입 실패`);
        console.error(`- userNumber: ${userNumber}`);
        console.error(`- status: ${res.status}`);
        console.error(`- body: ${res.body}`);

        if (res.status === 409) {
            console.error(`이미 존재하는 사용자`);
        } else if (res.status >= 500) {
            console.error(`서버 오류`);
        }
    } else {
        console.log(`회원가입 성공: ${userNumber}`);
    }

    sleep(0.1);
}
