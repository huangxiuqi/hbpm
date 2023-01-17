import { EventEmitter } from 'events';

export const PageLoadStart = 'start';
export const PageLoadFinish = 'finish';

/**
 * 页面加载事件
 */
const RouteLoadProgressEvent = new EventEmitter();
export default RouteLoadProgressEvent;
