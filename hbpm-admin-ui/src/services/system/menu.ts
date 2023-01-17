import request from '@/utils/request';

/**
 * 获取导航菜单
 */
export async function getMenu () {
  return request.get<API.Menu[]>('/api/menu');
}
