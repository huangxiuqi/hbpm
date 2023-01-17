namespace API {
  interface BaseResponse<T> {
    code: number;
    message: string;
    data: T;
  }

  interface Page<T> {
    list: T[];
    total: string;
  }

  interface PageReq extends Record<string, any> {
    page?: number;
    size?: number;
  }
}
