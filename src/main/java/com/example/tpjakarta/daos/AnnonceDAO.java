package com.example.tpjakarta.daos;

import com.example.tpjakarta.beans.Annonce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnonceDAO extends DAO<Annonce> {

    public AnnonceDAO(Connection con) {
        super(con);
    }

    @Override
    public boolean create(Annonce obj) {
        String req = "INSERT INTO annonce ( title,description, adress, mail, date) VALUES (? ,?, ?, ?, ?)";

        try(PreparedStatement statement = con.prepareStatement(req)) {
            statement.setString(1, obj.getTitle());
            statement.setString(2, obj.getDescription());
            statement.setString(3, obj.getAdress());
            statement.setString(4, obj.getMail());
            statement.setTimestamp(5, obj.getDate());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Annonce findById(Long id) {
        String req = "SELECT * FROM annonce WHERE id = ?";

        try (PreparedStatement statement = con.prepareStatement(req)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Annonce annonce = new Annonce();
                annonce.setId(rs.getLong("id"));
                annonce.setTitle(rs.getString("title"));
                annonce.setDescription(rs.getString("description"));
                annonce.setAdress(rs.getString("adress"));
                annonce.setMail(rs.getString("mail"));
                annonce.setDate(rs.getTimestamp("date"));
                return annonce;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Annonce> findAll() {
        List<Annonce> annonces = new ArrayList<>();
        String sql = "SELECT * FROM annonce";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Annonce annonce = new Annonce();
                annonce.setId(rs.getLong("id"));
                annonce.setTitle(rs.getString("title"));
                annonce.setDescription(rs.getString("description"));
                annonce.setAdress(rs.getString("adress"));
                annonce.setMail(rs.getString("mail"));
                annonce.setDate(rs.getTimestamp("date"));

                annonces.add(annonce);
                System.out.printf("annonce: %s",annonce);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all announcements: " + e.getMessage());
        }
        return annonces;
    }

    @Override
    public boolean update(Annonce obj) {
        String req = "UPDATE annonce SET title=?, description=?, adress=?, mail=?, date=? WHERE id = ?";

        try (PreparedStatement statement = con.prepareStatement(req)) {

            statement.setString(1, obj.getTitle());
            statement.setString(2, obj.getDescription());
            statement.setString(3, obj.getAdress());
            statement.setString(4, obj.getMail());
            statement.setTimestamp(5, obj.getDate());

            statement.setLong(6, obj.getId());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String req = "DELETE FROM annonce WHERE id = ?";

        try (PreparedStatement statement = con.prepareStatement(req)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
