package jpabook.jpashop.service;


import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;


import org.junit.Assert;
import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();

        Book book = createBook("라면",10000,10);
        //when
        int orderCount = 2;
       Long orderId =  orderService.order(member.getId(), book.getId(),  orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);


        Assert.assertEquals("상품 주문시 상태는 order",OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("주문한 상품 종류 수가 정확해야한다", 1 , getOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격은 가격 * 수량",book.getPrice() * orderCount,getOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야한다",8,book.getStockQuantity());//메세지,StickQuantity - orderCount = 값,호출


    }

    private Book createBook(String name,int price,int stockQuantity ) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","마포","404-12"));
        em.persist(member);
        return member;
    }
    

    @Test()
    public void 상품주문_재고수량초과() throws Exception {
        Member member = createMember();
        Item item = createBook("이재명", 5000, 10);
        int Count = 11;
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), Count);
        });



    }

    @Test
    public void 주문취소() throws Exception{
        //when
        Member member = createMember();
        Book item = createBook("이재명",3000,10);

        int Count = 3;


        //then
        Long orderid = orderService.order(member.getId(),item.getId(),Count);
        orderService.cancelOrder(orderid);


        //given
        Order getorder=orderRepository.findOne(orderid);

        Assert.assertEquals("주문취소",OrderStatus.CANCEL,getorder.getStatus());
        Assert.assertEquals("재고가 정상복구가되엇는지",10,item.getStockQuantity());


    }
    @Test
    public void 상품주문_재고수량초() throws Exception{

    }
}