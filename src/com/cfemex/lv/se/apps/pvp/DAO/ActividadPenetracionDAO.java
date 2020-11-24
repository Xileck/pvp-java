package com.cfemex.lv.se.apps.pvp.DAO;

import com.cfemex.lv.DAOException;
import com.cfemex.lv.libs.informix.Informix;
import com.cfemex.lv.se.apps.pvp.ActividadPenetracion;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cfemex.lv.libs.Utilerias.getValor;


public class ActividadPenetracionDAO {

    /**
     * Patr?n Singleton para devolver solo una instancia del objeto creado.
     *
     * @return Valor en el campo instance.
     */

    private static ActividadPenetracionDAO instance = null;

    public static ActividadPenetracionDAO getInstance() {
        if (instance == null)
            instance = new ActividadPenetracionDAO();
        return instance;
    }

    public List<ActividadPenetracion> seleccionarActividadPenetracion(List<String> condicion) {
        ActividadPenetracion ap = null;
        Informix q1 = new Informix("Cavam", "Informix/Cavam");
        List<ActividadPenetracion> lista = new ArrayList<ActividadPenetracion>();
        StringBuffer filtro = new StringBuffer("");
        try {
            if (!condicion.isEmpty()) {
                filtro.append(" WHERE ");

                for (String cadena : condicion) {
                    filtro.append(cadena + " AND ");

                }
                filtro.delete(filtro.lastIndexOf("AND", filtro.length()), filtro.length());
            }
            StringBuilder qry = new StringBuilder();
            qry.append("SELECT * FROM " + filtro);
            q1.setQry(qry.toString());
            ResultSet rs = q1.getRegisters();
            while (rs.next()) {
                ap = new ActividadPenetracion();

                lista.add(ap);
            }
        } catch (Exception e) {
            throw new DAOException(e);
        } finally {
            q1.desconectarBD();
            q1 = null;
        }
        return lista;
    }


}
