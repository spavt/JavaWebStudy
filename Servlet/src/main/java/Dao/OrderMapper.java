package Dao;

import Model.CRUD;
import Model.Student;
import Model.admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

// mybatis的接口
public interface OrderMapper {

    @Select("SELECT * FROM admin WHERE username = #{username} AND password = #{password}")
    admin findAdmin(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM students")
    List<Student> showStuDAO();

    @Delete("DELETE FROM students WHERE sno = #{sno}")
    int delStuDAO(String sno);

    @Insert("INSERT INTO students(sno, sname, ssex, sclass) VALUES(#{sno}, #{sname}, #{ssex}, #{sclass})")
    int addStuDAO(Student student);

    @Select("SELECT * FROM students WHERE sno = #{sno}")
    Student findStu(String sno);

    @Update("UPDATE students SET sname = #{sname}, ssex = #{ssex}, sclass = #{sclass} WHERE sno = #{sno}")
    int updateStu(Student student);

    @Select("SELECT COUNT(*) FROM students")
    int getTotalCount();

    @Select("SELECT * FROM students LIMIT #{start}, #{pageSize}")
    List<Student> showStuPage(@Param("start") int start, @Param("pageSize") int pageSize);

    @Insert("INSERT INTO admin(username, password) VALUES(#{username}, #{password})")
    int addAdmin(admin admin);

    @Select("SELECT * FROM CRUD")
    List<CRUD> showStuDAO1();

    @Insert("INSERT INTO CRUD(name,sex,age,tel,scores,school) VALUES(#{name},#{sex},#{age},#{tel},#{scores},#{school})")
    int insertStuDAO1(CRUD crud);

    @Delete("DELETE FROM CRUD WHERE id = #{id}")
    int delStuDAO1(int id);

    @Update("UPDATE CRUD SET name = #{name}, sex = #{sex}, age = #{age}, tel = #{tel}, scores = #{scores}, school = #{school} WHERE id = #{id}")
    int updateStu1(CRUD crud);

    @Select("SELECT * FROM CRUD LIMIT #{start}, #{pageSize}")
    List<CRUD> showCrudPage(@Param("start") int start, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM crud")
    int getTotalCount1();
}
