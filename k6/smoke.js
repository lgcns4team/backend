import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  stages: [
    { duration: "30s", target: 5 },
    { duration: "60s", target: 20 },
    { duration: "30s", target: 0 },
  ],
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<500"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://backend:8080/nok-nok";

export default function () {
  const res = http.get(`${BASE_URL}/api/categories-with-menus`);
  check(res, { "status is 200": (r) => r.status === 200 });
  sleep(1);
}
