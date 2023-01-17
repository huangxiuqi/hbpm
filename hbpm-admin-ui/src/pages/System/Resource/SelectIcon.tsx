import React, { useState } from 'react';
import { Button, Input, Pagination, Popover } from 'antd';
import * as Icons from '@ant-design/icons';
import style from './SelectIcon.module.less';

export interface SelectIconComponentPropsType {
  icon?: React.ReactNode
  onChange?: (name: string) => void
}

const check = (name: string) => {
  const reg = /^[A-Z][A-z0-9]*$/;
  return reg.test(name);
};

const pageSize = 99;
const iconList: string[] = [];
for (const name in Icons) {
  if (check(name) && name.endsWith('Outlined')) {
    iconList.push(name);
  }
}

const SelectIconContent: React.FC<SelectIconComponentPropsType> = props => {
  const [pageIndex, setPageIndex] = useState(1);
  const [search, setSearch] = useState('');
  const searchIconList = iconList.filter(i => i.toLowerCase().includes(search.toLowerCase()));
  const total = searchIconList.length;
  const icons = searchIconList.slice((pageIndex - 1) * pageSize, pageIndex * pageSize);

  const searchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value: inputValue } = e.target;
    setSearch(inputValue);
  };

  return (
    <>
      <div className={style.selectIconHeader}>
        <Input onInput={searchChange} value={search} placeholder="输入图标名称搜索"/>
      </div>
      <div className="select-icon-content">
        <div className={style.selectIconScroll}>
          <ul className={style.selectIconList}>
            {icons.map(i =>
              <li
                className={style.selectIconItem}
                key={i}
                onClick={() => {
                  if (props.onChange) {
                    props.onChange(i);
                  }
                }}
              >{React.createElement((Icons as any)[i])}</li>,
            )}
          </ul>
        </div>
        <div className={style.selectIconPagination}>
          <Pagination
            defaultCurrent={pageIndex}
            total={total}
            showLessItems={true}
            pageSize={pageSize}
            onChange={(page, pageSize) => {
              setPageIndex(page);
            }}
            showSizeChanger={false}/>
        </div>
      </div>
    </>
  );
};

const SelectIcon: React.FC<SelectIconComponentPropsType> = props => {
  const [open, setOpen] = useState(false);

  return (
    <Popover
      overlayClassName="select-icon-popover"
      placement="bottomRight"
      trigger="click"
      content={<SelectIconContent onChange={name => {
        if (props.onChange) {
          props.onChange(name);
        }
        setOpen(false);
      }}/>}
      open={open}
      onOpenChange={setOpen}
    >
      <Button icon={props.icon}/>
    </Popover>
  );
};

export default SelectIcon;
