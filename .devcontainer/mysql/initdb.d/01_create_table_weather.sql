create table meteorology (
  id int not null primary key auto_increment comment 'ID',
  prefecture varchar(8) comment '都道府県',
  measure_date date comment '測定日',
  heighest_temperature float comment '最高気温',
  lowest_temperature float comment '最低気温',
  average_temperature float comment '平均気温',
  created_at datetime comment '作成日時',
  updated_at datetime comment '更新日時',
  deleted_at datetime comment '削除日時',
  delete_flag boolean default false comment '削除フラグ'
) comment='気象';