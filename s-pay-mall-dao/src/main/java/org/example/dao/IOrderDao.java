package org.example.dao;


import org.apache.ibatis.annotations.Mapper;
import org.example.domain.po.PayOrder;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);

}
