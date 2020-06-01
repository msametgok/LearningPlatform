package com.project.focustime;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.project.focustime.Activities.SignInActivity;
import com.project.focustime.Models.Category;
import com.project.focustime.Models.Course;
import com.project.focustime.Models.Lesson;
import com.project.focustime.Models.MyCourse;
import com.project.focustime.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBOperations {
    Statement statement;
    ResultSet rs;
    PreparedStatement ps;

    ArrayList<Course> course = new ArrayList<>();
    ArrayList<Category> category = new ArrayList<>();
    ArrayList<Lesson> lessons = new ArrayList<>();
    ArrayList<MyCourse> mycourses = new ArrayList<>();
    ArrayList<Course> wishlist = new ArrayList<>();


    public ArrayList<User> signIn(Connection conn,String email, String password ){

        ArrayList<User> user = new ArrayList<User>();

        String myQuery = "Select * from users where user_email = ? and user_password = ?";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setString(1,email);
            ps.setString(2, password);

            rs = ps.executeQuery();

            while(rs.next()){

                 user.add( new User(rs.getInt(1),rs.getString(2),rs.getString(3),
                         rs.getString(6),rs.getString(7),1,
                         rs.getString(10), rs.getString(11), rs.getString(12),
                         rs.getString(13),rs.getString(14),rs.getString(8)));

            }

            return user;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return null;
        }

    }

    public void signUp(Connection conn, String name, String password, String mail){

        String myQuery = "Insert Into users (user_name,user_password,user_verifycode,user_email,user_time,user_website," +
                "user_github,user_linkedin,user_description) VALUES (?,?,?,?,?,?,?,?,?) ";

        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        java.sql.Timestamp date2 = new java.sql.Timestamp(date.getTime());

        try {
            ps = conn.prepareStatement(myQuery);
            //preparedStatement.setString(1,"null");
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, "");
            ps.setString(4,mail);
            ps.setTimestamp(5,date2);
            ps.setString(6,"");
            ps.setString(7,"");
            ps.setString(8,"");
            ps.setString(9,"");

            ps.executeUpdate();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("BAŞARISIZ İŞLEM");
        }

    }

    public boolean checkUser(Connection conn, String mail){

        String myQuery = "Select user_email from users where user_email = ?";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setString(1,mail);


            rs = ps.executeQuery();

            int i=0;

            while(rs.next()){
                i++;
            }
            if(i==0){
                return false;
            }
            else{
                return true;
            }


        } catch (Exception e) {
            Log.e("Mail Bulunamadı",e.toString());
            return false;
        }
    }

    public ArrayList<Course> getCourses(Connection conn){

        String myQuery = "SELECT c.course_id,course_instructor,course_category,course_title,course_url,course_shortdescription," +
                "course_description,course_requirements,course_outcomes,course_photo,course_level,course_price, " +
                "IFNULL(AVG(rating_value),0) from courses c left outer JOIN ratings r on c.course_id = r.course_id " +
                "where course_status= 1 GROUP BY course_id";

        try {
            ps = conn.prepareStatement(myQuery);

            rs = ps.executeQuery();

            while(rs.next()){

                course.add( new Course(rs.getInt(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7), rs.getString(8), rs.getString(9),
                        rs.getString(10),rs.getString(11),rs.getString(12),
                        rs.getInt(13),0,0,"English",""));
            }

            return course;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return null;
        }

    }

    public ArrayList<Category> getCategory (Connection conn){

        String myQuery = "SELECT category_id, category_name,category_icon,COUNT(course_category) from category c1 inner join" +
                " courses c2 on c1.category_name=c2.course_category GROUP by course_category";

        try {
            ps = conn.prepareStatement(myQuery);

            rs = ps.executeQuery();

            while(rs.next()){

                category.add( new Category(rs.getInt(1),rs.getString(2),"",rs.getInt(4)));

            }

            return category;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return null;
        }

    }

    public ArrayList<Lesson> getLesson (Connection conn, int courseId){

        String myQuery = "SELECT * from lessons where course_id = ?";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,courseId);
            rs = ps.executeQuery();

            while(rs.next()){

                lessons.add( new Lesson(rs.getInt(1),rs.getString(3),rs.getInt(2),
                        rs.getString(5),"video",rs.getInt(6),"",0,"youtube"));

            }

            return lessons;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return null;
        }

    }

    public void enroll(Connection conn, int user_id, int course_id){

        String myQuery = "insert INTO enroll (user_id,course_id,enroll_time) values (?,?,?)";

        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        java.sql.Timestamp date2 = new java.sql.Timestamp(date.getTime());

        try {
            ps = conn.prepareStatement(myQuery);
            //preparedStatement.setString(1,"null");
            ps.setInt(1, user_id);
            ps.setInt(2, course_id);
            ps.setTimestamp(3,date2);

            ps.executeUpdate();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<MyCourse> getMyCourses (Connection conn, int user_id){

        String myQuery = "SELECT c.course_id, course_instructor,course_title,course_url,course_photo,course_price,user_id FROM courses c " +
                      "inner join enroll e on c.course_id=e.course_id where user_id=?";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,user_id);
            rs = ps.executeQuery();

            while(rs.next()){

                mycourses.add( new MyCourse(rs.getInt(1),rs.getString(3),rs.getString(5),
                        rs.getString(6),rs.getString(2),0,0,0,
                        0,0,0,rs.getString(4)));
            }

            return mycourses;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return null;
        }

    }

    public boolean isMyCourse(Connection conn, int course_id, int user_id){
        String myQuery = "SELECT * from enroll where course_id= ? and user_id=? ";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,course_id);
            ps.setInt(2,user_id);
            rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
            else{
                return false;
            }


        } catch (Exception e) {
            Log.e("Hata BURADA",e.toString());
            return false;
        }
    }

    public boolean isAdded(Connection conn, int course_id, int user_id){
        String myQuery = "SELECT * from likes where course_id= ? and user_id=? ";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,course_id);
            ps.setInt(2,user_id);
            rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
            else{
                return false;
            }


        } catch (Exception e) {
            Log.e("Hata BURADA",e.toString());
            return false;
        }
    }

    public void addWishlist(Connection conn, int course_id, int user_id){

        String myQuery = "INSERT INTO likes (user_id, course_id) VALUES(?,?)";

        try{
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,user_id);
            ps.setInt(2,course_id);

            ps.executeUpdate();

        }catch (Exception e){

        }
    }
    public ArrayList<Course> getWishlist (Connection conn, int user_id){

        String myQuery = "SELECT * FROM courses inner join likes on courses.course_id=likes.course_id where user_id=?";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,user_id);
            rs = ps.executeQuery();

            while(rs.next()){

                wishlist.add( new Course(rs.getInt(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8), rs.getString(9),rs.getString(10),rs.getString(11),
                        rs.getString(12),0,0,0,"",""));
            }

            return wishlist;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return null;
        }

    }

    public void deleteWishlist(Connection conn, int course_id, int user_id){

        String myQuery = "DELETE FROM likes WHERE course_id = ? and user_id = ?";

        try{
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,course_id);
            ps.setInt(2,user_id);

            ps.executeUpdate();

        }catch (Exception e){

        }
    }

    public void changePass(Connection conn, int user_id, String pass){

        String myQuery = "update users set user_password = ? where user_id = ?";

        try{
            ps = conn.prepareStatement(myQuery);
            ps.setString(1,pass);
            ps.setInt(2,user_id);

            ps.executeUpdate();

        }catch (Exception e){

        }

    }

    public void editProfile(Connection conn, int user_id, String user_name, String bio, String website, String github, String linkedin){

        String myQuery = "UPDATE users set user_name=?, user_description=?, user_website=?, user_github=?,user_linkedin=? where user_id=?";

        try{
            ps = conn.prepareStatement(myQuery);
            ps.setString(1,user_name);
            ps.setString(2,bio);
            ps.setString(3,website);
            ps.setString(4,github);
            ps.setString(5,linkedin);
            ps.setInt(6,user_id);

            ps.executeUpdate();

        }catch (Exception e){

        }

    }
    public int getEnrollment (Connection conn, int course_id){

        int a=0;
        String myQuery = "SELECT count(user_id) FROM enroll where course_id=?";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,course_id);
            rs = ps.executeQuery();

            while(rs.next()){

                 a = rs.getInt(1);
            }

            return a;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return 0;
        }

    }

    public int getLessonNumber (Connection conn, int course_id){

        int a=0;
        String myQuery = "SELECT COUNT(lesson_title) FROM lessons WHERE course_id=?";

        try {
            ps = conn.prepareStatement(myQuery);
            ps.setInt(1,course_id);
            rs = ps.executeQuery();

            while(rs.next()){

                a = rs.getInt(1);
            }

            return a;

        } catch (Exception e) {
            Log.e("Hata",e.toString());
            return 0;
        }

    }


}
