import request from '../utils/request';

export async function login(data: API.Login) {
  return request.post('/api/login/password', null, {
    params: {
      username: data.username,
      password: data.password,
      captchaId: data.captchaId,
      captchaValue: data.captchaValue,
    },
  });
}

export async function getCaptcha() {
  let timestamp = new Date().getTime();
  return request.get('/api/captcha/generate/gif', {
    params: {
      width: 128,
      height: 48,
      t: timestamp,
    },
  })
}
