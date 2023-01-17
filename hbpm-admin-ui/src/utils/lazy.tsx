import React from 'react';
import RouteLoadProgressEvent from '@/events/page-load-event';

type InputType = React.ReactElement | React.FC;

/**
 * 已加载模块标记
 * 防止重复显示加载进度条
 */
const loaded: WeakSet<() => Promise<InputType>> = new WeakSet();

/**
 * 路由懒加载
 * @param loader
 */
const lazy = (loader: () => Promise<InputType>) => {
  class Content extends React.Component<any, any> {
    constructor(props: any) {
      super(props);
      this.state = {
        comp: undefined,
      };
    }

    needLoad = false;

    emitStartEvent() {
      if (this.needLoad) {
        return;
      }
      RouteLoadProgressEvent.emit('start');
    }

    emitFinishEvent() {
      if (this.needLoad) {
        return;
      }
      RouteLoadProgressEvent.emit('finish');
    }

    componentDidMount() {
      if (loaded.has(loader)) {
        this.needLoad = true;
      }
      this.emitStartEvent();
      void loader()
        .then(c => {
          loaded.add(loader);
          this.setState({
            comp: c,
          });
        })
        .finally(() => {
          this.emitFinishEvent();
        });
    }

    render() {
      const { comp } = this.state;
      return comp ? React.createElement(comp, this.props) : null;
    }
  }
  return <Content/>;
};

export default lazy;
