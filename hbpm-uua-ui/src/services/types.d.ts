declare namespace API {
  type BaseResponse<T> = {
    code: number;
    message: string;
    data: T;
  }

  type Login = {
    username: string;
    password: string;
    captchaId: string;
    captchaValue: string;
  }
}
