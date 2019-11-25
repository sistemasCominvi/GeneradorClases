package com.cominvi.app.capitalhumano.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.cominvi.app.commons.models.Empleados;

@Repository
public class JdbcEmpleadosRepository {

  @Autowired public JdbcTemplate jdbcTemplate;

  private final String tabla = "capitalhumano.dbo.empleados";

  private final String insert =
      "INSERT INTO "
          + tabla
          + " "
          + "(apellidomaterno,apellidopaterno,estatus,fechahoraalta,fechahoramod,fechanacimiento,idempleadoalta,idempleadomod,nombre,numeronomina,rfc) "
          + "values (?,?,?,?,?,?,?,?,?,?,?)";

  public List<Empleados> findAll() {
    return jdbcTemplate.query("SELECT * FROM " + tabla, new EmpleadosRowMapper());
  }

  public Empleados findById(Long idempleado) {
    return jdbcTemplate.queryForObject(
        "SELECT * FROM " + tabla + " WHERE idempleado = " + idempleado, new EmpleadosRowMapper());
  }

  public Empleados save(final Empleados empleado) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, empleado.getApellidomaterno());
            ps.setString(2, empleado.getApellidopaterno());
            ps.setInt(3, empleado.getEstatus());
            ps.setString(4, empleado.getFechahoraalta());
            ps.setDate(5, new java.sql.Date(empleado.getFechahoramod().getTime()));
            ps.setString(6, empleado.getFechanacimiento());
            ps.setLong(7, empleado.getIdempleadoalta());
            ps.setLong(8, empleado.getIdempleadomod());
            ps.setString(9, empleado.getNombre());
            ps.setInt(10, empleado.getNumeronomina());
            ps.setString(11, empleado.getRfc());

            return ps;
          }
        },
        keyHolder);

    empleado.setIdempleado(keyHolder.getKey().longValue());

    return empleado;
  }

  public Boolean saveAll(final List<Empleados> empleados) {

    jdbcTemplate.batchUpdate(
        insert,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            Empleados empleado = empleados.get(i);
            ps.setString(1, empleado.getApellidomaterno());
            ps.setString(2, empleado.getApellidopaterno());
            ps.setInt(3, empleado.getEstatus());
            ps.setString(4, empleado.getFechahoraalta());
            ps.setDate(5, new java.sql.Date(empleado.getFechahoramod().getTime()));
            ps.setString(6, empleado.getFechanacimiento());
            ps.setLong(7, empleado.getIdempleadoalta());
            ps.setLong(8, empleado.getIdempleadomod());
            ps.setString(9, empleado.getNombre());
            ps.setInt(10, empleado.getNumeronomina());
            ps.setString(11, empleado.getRfc());
          }

          @Override
          public int getBatchSize() {
            return empleados.size();
          }
        });

    return true;
  }

  public Empleados update(final Empleados empleado, Long idempleado) {
    String sql =
        "UPDATE "
            + tabla
            + " SET apellidomaterno = ?,apellidopaterno = ?,estatus = ?,fechahoraalta = ?,fechahoramod = ?,fechanacimiento = ?,idempleadoalta = ?,idempleadomod = ?,nombre = ?,numeronomina = ?,rfc = ?  WHERE idempleado = ?";

    jdbcTemplate.update(
        sql,
        new Object[] {
          empleado.getApellidomaterno(),
          empleado.getApellidopaterno(),
          empleado.getEstatus(),
          empleado.getFechahoraalta(),
          empleado.getFechahoramod(),
          empleado.getFechanacimiento(),
          empleado.getIdempleadoalta(),
          empleado.getIdempleadomod(),
          empleado.getNombre(),
          empleado.getNumeronomina(),
          empleado.getRfc()
        });

    return empleado;
  }

  public Boolean removeById(Long idempleado) {
    String delete = "DELETE FROM " + tabla + " WHERE idempleado = ?";
    jdbcTemplate.update(delete, new Object[] {idempleado});
    return true;
  }

  private static class EmpleadosRowMapper implements RowMapper<Empleados> {

    @Override
    public Empleados mapRow(ResultSet rs, int rowNum) throws SQLException {

      Empleados empleado = new Empleados();
      empleado.setIdempleado(rs.getLong("idempleado"));
      empleado.setApellidomaterno(rs.getString("apellidomaterno"));
      empleado.setApellidopaterno(rs.getString("apellidopaterno"));
      empleado.setEstatus(rs.getInt("estatus"));
      empleado.setFechahoraalta(rs.getString("fechahoraalta"));
      empleado.setFechahoramod(rs.getDate("fechahoramod"));
      empleado.setFechanacimiento(rs.getString("fechanacimiento"));
      empleado.setIdempleadoalta(rs.getLong("idempleadoalta"));
      empleado.setIdempleadomod(rs.getLong("idempleadomod"));
      empleado.setNombre(rs.getString("nombre"));
      empleado.setNumeronomina(rs.getInt("numeronomina"));
      empleado.setRfc(rs.getString("rfc"));

      return empleado;
    }
  }
}
