import { MenuProps } from 'antd';
import React from 'react';
import { Link } from 'react-router-dom';
import * as Icons from '@ant-design/icons';

/**
 * 将请求数据转换为Antd menu
 * @param menu
 */
export const transform = (menu: API.Menu[] | undefined): MenuProps['items'] => {
  function transformHelper (menu: API.Menu[] | undefined): MenuProps['items'] {
    if (menu == null) {
      return [];
    }
    return menu.map(i => {
      const obj: any = {
        key: i.id,
        label: i.name,
        title: i.name,
      };

      if (i.icon) {
        obj.icon = React.createElement((Icons as any)[i.icon]);
      }

      let hasChildren = false;
      const children = transformHelper(i.children);
      if (children && children.length > 0) {
        obj.children = children;
        hasChildren = true;
      }

      if (i.url !== undefined && !hasChildren) {
        const MenuItem: React.FC = () => {
          return <Link
            to={i.url!}
            target={i.newWindow ? '_blank' : '_self'}
          >{i.name}</Link>;
        };
        obj.label = <MenuItem/>;
      }
      return obj;
    });
  }

  return transformHelper(menu);
};

/**
 * 根据路径查询菜单
 * @param menu 菜单列表
 * @param path 路径
 */
export const findMenuByPath = (menu: API.Menu[] | undefined, path: string): API.Menu[] => {
  function findMenuByPathHelper (menu: API.Menu[] | undefined, path: string, result: API.Menu[]): boolean {
    if (menu === undefined) {
      return false;
    }
    for (let i = 0; i < menu.length; i++) {
      if (menu[i].url === path) {
        result.push(menu[i]);
        return true;
      }
      if (findMenuByPathHelper(menu[i].children, path, result)) {
        return true;
      }
    }
    return false;
  }

  const result: API.Menu[] = [];
  findMenuByPathHelper(menu, path, result);
  return result;
};

/**
 * 根据路径查询所有祖先节点，不包括自身
 * @param menu
 * @param path
 */
export const findAncestorByPath = (menu: API.Menu[] | undefined, path: string): API.Menu[] => {
  function findAncestorByPathHelper (menu: API.Menu[] | undefined, path: string, result: API.Menu[]): boolean {
    if (menu === undefined) {
      return false;
    }
    for (let i = 0; i < menu.length; i++) {
      if (menu[i].url === path) {
        return true;
      }
      if (findAncestorByPathHelper(menu[i].children, path, result)) {
        result.push(menu[i]);
        return true;
      }
    }
    return false;
  }

  const result: API.Menu[] = [];
  findAncestorByPathHelper(menu, path, result);
  return result;
};

/**
 * 根据路径查找
 * @param menu
 * @param path
 */
export const findAllByPath = (menu: API.Menu[] | undefined, path: string): API.Menu[] => {
  function findAllByPathHelper (menu: API.Menu[] | undefined, path: string, result: API.Menu[]): boolean {
    if (menu === undefined) {
      return false;
    }
    for (let i = 0; i < menu.length; i++) {
      if (findAllByPathHelper(menu[i].children, path, result)) {
        result.push(menu[i]);
        return true;
      }
      if (menu[i].url === path) {
        result.push(menu[i]);
        return true;
      }
    }
    return false;
  }

  const result: API.Menu[] = [];
  findAllByPathHelper(menu, path, result);
  return result;
};
