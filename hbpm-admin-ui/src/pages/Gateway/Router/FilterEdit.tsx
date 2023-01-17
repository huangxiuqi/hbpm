import React from 'react';
import { Button } from 'antd';

interface FilterEditProps {
  value?: string;
  onChange?: (value: string) => void;
}

const FilterEdit: React.FC<FilterEditProps> = props => {
  if (props.value) {
    console.log(JSON.parse(props.value));
  }
  return (
    <>
      <div><Button type="primary">添加过滤器</Button></div>
    </>
  );
};

export default FilterEdit;
