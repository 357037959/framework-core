package com.linda.framework.core.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctvit.framework.core.dao.GenericDao;
import com.ctvit.framework.core.dao.query.Conditions;
import com.ctvit.framework.core.dao.query.OrderPart;
import com.ctvit.framework.core.dao.query.Query;

import demo.vo.Room;
import demo.vo.RoomConst;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/conf/applicationContext.xml")
@Ignore
public class GenericDaoTest {
	@Autowired
	GenericDao baseDao;

	
	@Before
	public void setUp() throws Exception {
		List<Room> beans=new ArrayList<Room>();
		for(int i=1;i<21;i++){
			Room room = new Room();
			room.setBuildingId(7L);
			room.setRoomNo(""+(2000+i));
			beans.add(room);
		}
		baseDao.batchInsert(beans);
	}

	@After
	public void tearDown() throws Exception {
		baseDao.deleteByExample(Room.class, null);
	}
	
	@Test
	public final void testInsertString() {
		Room room = new Room();
		room.setBuildingId(7L);
		int ret = baseDao.insert("demo.dao.sqlmap.db2.RoomMapper.insert",
				room);
		assertTrue(ret > 0);
	}

	@Test
	public final void testInsertT() {
		Room room = new Room();
		room.setBuildingId(7L);
		room.setCreateTime(new Date());
		int ret = baseDao.insert(room);
		assertTrue(ret > 0);
	}

	@Test
	public final void testBatchInsert() {
		List<Room> beans=new ArrayList<Room>();
		for(int i=0;i<20;i++){
			Room room = new Room();
			room.setBuildingId(7L);
			room.setRoomNo("batchInsert");
			beans.add(room);
		}
		baseDao.batchInsert(beans);
		//
		Conditions conditions = Conditions.and().equalTo(
				RoomConst.COLUMN_ROOM_NO, "batchInsert");
		int ret = baseDao.deleteByExample(Room.class, conditions);
		assertTrue(ret==20);
	}

	@Test
	public final void testBatchUpdate() {
		//
		List<Room> beans=new ArrayList<Room>();
		for(int i=0;i<20;i++){
			Room room = new Room();
			room.setBuildingId(7L);
			room.setRoomNo("batchInsert");
			beans.add(room);
		}
		baseDao.batchInsert(beans);

		//
		Query query=Query.create().setConditions(Conditions.and().equalTo(RoomConst.COLUMN_ROOM_NO, "batchInsert"));
		List<Room> result=baseDao.selectByExample(Room.class, query, true);
		for(Room room:result){
			room.setRoomNo("batchUpdate");
			beans.add(room);
		}
		baseDao.batchUpdate(beans,true);
		//
		Conditions conditions = Conditions.and().equalTo(
				RoomConst.COLUMN_ROOM_NO, "batchUpdate");
		int ret = baseDao.deleteByExample(Room.class, conditions);
		assertTrue(ret==20);
	}

	@Test
	public final void testUpdate() {
		Room room0 = new Room();
		room0.setBuildingId(7L);
		room0.setCreateTime(new Date());
		baseDao.insert(room0);
		
		Room room = new Room();
		room.setRoomId(room0.getRoomId());
		room.setBuildingId(8L);
		byte[] image = "abcd".getBytes();
		room.setImage(image);
		int ret = baseDao.update(
				"demo.dao.sqlmap.db2.RoomMapper.updateByPrimaryKey", room);

		assertNotNull(ret);
		assertTrue(ret > 0);
	}

	@Test
	public final void testUpdateByPrimaryKey() {
		Room room0 = new Room();
		room0.setBuildingId(7L);
		room0.setCreateTime(new Date());
		baseDao.insert(room0);
		//
		Room room = new Room();
		room.setRoomId(room0.getRoomId());
		room.setBuildingId(8L);
		byte[] image = "abcd".getBytes();
		room.setImage(image);
		int ret = baseDao.updateByPrimaryKey(room, false);

		assertNotNull(ret);
		assertTrue(ret > 0);
	}

	@Test
	public final void testUpdateByPrimaryKey2() {
		Room room0 = new Room();
		room0.setBuildingId(7L);
		room0.setCreateTime(new Date());
		baseDao.insert(room0);
		//
		Room room = new Room();
		room.setRoomId(room0.getRoomId());
		room.setBuildingId(8L);
		byte[] image = "abcd".getBytes();
		room.setImage(image);
		int ret = baseDao.updateByPrimaryKey(room, true);

		assertNotNull(ret);
		assertTrue(ret > 0);
	}

	@Test
	public final void testUpdateByPrimaryKeySelective() {
		Room room0 = new Room();
		room0.setBuildingId(7L);
		room0.setCreateTime(new Date());
		baseDao.insert(room0);
		
		Room room = new Room();
		room.setRoomId(room0.getRoomId());
		room.setBuildingId(8L);
		byte[] image = "abcd".getBytes();
		room.setImage(image);
		int ret = baseDao.updateByPrimaryKeySelective(room);

		assertNotNull(ret);
		assertTrue(ret > 0);
	}

	@Test
	public final void testUpdateByExample() {
		Room room = new Room();
		room.setBuildingId(7L);
		room.setCreateTime(new Date());
		baseDao.insert(room);
		
		Conditions conditions = Conditions.and().equalTo(
				RoomConst.COLUMN_ROOM_ID, room.getRoomId());
		Room room2 = new Room();
		room2.setRoomId(room.getRoomId());
		room2.setBuildingId(7L);
		byte[] image = "abcd".getBytes();
		room2.setImage(image);
		int ret = baseDao.updateByExample(room2, conditions, false);

		assertNotNull(ret);
		assertTrue(ret > 0);
		System.out.println(ret);
	}

	@Test
	public final void testUpdateByExample2() {
		Room room0 = new Room();
		room0.setBuildingId(7L);
		room0.setCreateTime(new Date());
		baseDao.insert(room0);
		
		
		Conditions conditions = Conditions.and().equalTo(
				RoomConst.COLUMN_ROOM_ID, room0.getRoomId());
		Room room = new Room();
		room.setRoomId(room0.getRoomId());
		room.setBuildingId(7L);
		byte[] image = "abcd".getBytes();
		room.setImage(image);
		int ret = baseDao.updateByExample(room, conditions, true);

		assertNotNull(ret);
		assertTrue(ret > 0);
		System.out.println(ret);
	}

	@Test
	public final void testUpdateByExampleSelective() {
		Room room0 = new Room();
		room0.setBuildingId(7L);
		room0.setCreateTime(new Date());
		baseDao.insert(room0);
		
		Conditions conditions = Conditions.and().equalTo(
				RoomConst.COLUMN_ROOM_ID, room0.getRoomId());
		Room room = new Room();
		room.setRoomNo("test");
		byte[] image = "abcd".getBytes();
		room.setImage(image);
		int ret = baseDao.updateByExampleSelective(room, conditions);

		assertNotNull(ret);
		assertTrue(ret > 0);
		System.out.println(ret);
	}

	@Test
	public final void testDelete() {
		
		Room room = new Room();
		room.setBuildingId(7L);
		int ret = baseDao.insert(room);
		assertTrue(ret > 0);
		ret = baseDao.delete(
				"demo.dao.sqlmap.db2.RoomMapper.deleteByPrimaryKey",
				room.getRoomId());
		assertTrue(ret > 0);
	}

	@Test
	public final void testDeleteByPrimaryKey() {
		Room room = new Room();
		room.setBuildingId(7L);
		int ret = baseDao.insert(room);
		assertTrue(ret > 0);
		ret = baseDao.deleteByPrimaryKey(Room.class, room.getRoomId());
		assertTrue(ret > 0);
		
	}

	@Test
	public final void testDeleteByExample() {
	
		Room room = new Room();
		room.setBuildingId(7L);
		int ret = baseDao.insert(room);
		
		Conditions conditions = Conditions.and().equalTo(
				RoomConst.COLUMN_BUILDING_ID, 7L);
		ret = baseDao.deleteByExample(Room.class, conditions);

		assertTrue(ret > 0);
	}

	@Test
	public final void testSelectListStringObject() {
		
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		List<?> ret = baseDao.selectList("demo.dao.sqlmap.db2.RoomMapper.selectByExample", query);

		assertNotNull(ret);
		assertTrue(ret.size() > 0);
	}
	@Test
	public final void testSelectListStringObject2() {
		
		List<?> ret = baseDao.selectList("demo.dao.sqlmap.db2.RoomMapper.selectByExample", null);

		assertNotNull(ret);
		assertTrue(ret.size() > 0);
	}
	@Test
	public final void testSelectListStringObject3() {
		Query query = Query.create();
		query.setOrderBy(OrderPart.create().asc("ROOM_NO"));
		List<?> ret = baseDao.selectList("demo.dao.sqlmap.db2.RoomMapper.selectByExample", query);

		assertNotNull(ret);
		assertTrue(ret.size() > 0);
	}
	@Test
	public final void testSelectListStringObjectRowBounds() {
		
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		query.setOrderBy(OrderPart.create().asc("ROOM_NO"));
		RowBounds rowBound=new RowBounds(4,5);
		List<?> ret = baseDao.selectList("demo.dao.sqlmap.db2.RoomMapper.selectByExample", query,rowBound);

		assertNotNull(ret);
		assertTrue(ret.size() ==5);
		
	}

	@Test
	public final void testSelectOne() {
		Room room = new Room();
		room.setBuildingId(7L);
		baseDao.insert(room);
		Object answer = baseDao.selectOne("demo.dao.sqlmap.db2.RoomMapper.selectByPrimaryKey",room.getRoomId());

		assertNotNull(answer);
	}

	@Test
	public final void testSelectMapStringObjectString() {
		
		//
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		Map<?,?> ret = baseDao.selectMap("demo.dao.sqlmap.db2.RoomMapper.selectByExample", query,"roomId");

		assertNotNull(ret);
		assertTrue(ret.size() >0);
	}

	@Test
	public final void testSelectMapStringObjectStringRowBounds() {
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		RowBounds rowBound=new RowBounds(0,3);
		Map<?,?> ret = baseDao.selectMap("demo.dao.sqlmap.db2.RoomMapper.selectByExample", query,"roomId",rowBound);

		assertNotNull(ret);
		assertTrue(ret.size() >0);
	}
	@Test
	public final void testSelectMapByExampleClassOfTQueryBoolean() {
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		Map<?,Room> ret = baseDao.selectMapByExample(Room.class, query,"roomId", false);

		assertNotNull(ret);
		assertTrue(ret.size() >0);
	}	
	@Test
	public final void testSelectMapByExampleClassOfTQueryRowBoundsBoolean() {
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		RowBounds rowBound=new RowBounds(0,3);
		Map<?,Room> ret = baseDao.selectMapByExample(Room.class, query,"roomId",rowBound, false);

		assertNotNull(ret);
		assertTrue(ret.size() == 3);
	}
	@Test
	public final void testSelectByPrimaryKey() {
		Room room = new Room();
		room.setBuildingId(7L);
		baseDao.insert(room);
		Room answer = baseDao.selectByPrimaryKey(Room.class,room.getRoomId());

		assertNotNull(answer);
	}

	@Test
	public final void testSelectByExampleClassOfTQueryRowBoundsBoolean() {
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		RowBounds rowBound=new RowBounds(0,3);
		List<Room> ret = baseDao.selectByExample(Room.class, query,rowBound, false);

		assertNotNull(ret);
	}

	@Test
	public final void testSelectByExampleClassOfTQueryBoolean() {
		Query query = Query.create();
		Conditions conditions = Conditions.and().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		List<Room> ret = baseDao.selectByExample(Room.class, query, false);

		assertNotNull(ret);
		assertTrue(ret.size() > 0);
	}

	@Test
	public final void testSelectByExampleClassOfTQueryBoolean2() {
		Query query = Query.create();
		Conditions conditions = Conditions.or().greaterThan("ROOM_NO", "2000")
				.equalTo("BUILDING_ID", 7);
		query.setConditions(conditions);
		List<Room> ret = baseDao.selectByExample(Room.class, query, false);

		assertNotNull(ret);
		assertTrue(ret.size() > 0);
		System.out.println(ret.size());
	}

	@Test
	public final void testSelectByExampleClassOfTQueryBoolean3() {
		Query query = Query.create();
		Conditions conditions = Conditions
				.and()
				.greaterThan("ROOM_NO", "2000")
				.addConditions(
						Conditions.or()
								.greaterThan(RoomConst.COLUMN_ROOM_NO, "2000")
								.lessThan(RoomConst.COLUMN_ROOM_NO, "1000"))
				.addConditions(
						Conditions
								.or()
								.equalTo("BUILDING_ID", 7)
								.equalTo("BUILDING_ID", 9)
								.addConditions(
										Conditions
												.or()
												.greaterThan(
														RoomConst.COLUMN_BUILDING_ID,
														5)
												.lessThan(
														RoomConst.COLUMN_BUILDING_ID,
														1000)));
		query.setConditions(conditions);
		List<Room> ret = baseDao.selectByExample(Room.class, query, false);

		assertNotNull(ret);
		assertTrue(ret.size() > 0);
		System.out.println(ret.size());
	}
}
