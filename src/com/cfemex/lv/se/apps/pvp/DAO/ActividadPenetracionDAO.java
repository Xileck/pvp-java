package com.cfemex.lv.se.apps.pvp.DAO;

import com.cfemex.lv.DAOException;
import com.cfemex.lv.libs.Utilerias;
import com.cfemex.lv.libs.informix.Informix;
import com.cfemex.lv.se.apps.pvp.ActividadPenetracion;
import com.cfemex.lv.se.apps.pvp.RelacionPenetValvulas;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
            qry.append("SELECT trim(a.cveactiv), " +
                    "       trim(a.numorden), " +
                    "       trim(a.tag)  as ubicacion, " +
                    "       trim(eq.denequipo)  AS denom_equipo, " +
                    "       trim(a.descactiv), " +
                    "       trim(a.sist) AS sistema, " +
                    "       trim(a.div)  AS division, " +
                    "       trim(gpo.descripcion), " +
                    "       trim(a.area), " +
                    "       trim(a.cveingresp), " +
                    "       a.fechapini, " +
                    "       a.fechapfin, " +
                    "       a.fecharini, " +
                    "       a.fecharfin, " +
                    "       a.porcentaje,"+
                    "       (a.fechapfin -  a.fechapini) as dur_original," +
                    "       tag.cvecomp," +
                    "       tag.consectag " +
                    " FROM admgcn.r3activ a " +
                    "         LEFT JOIN admgcn.planegpotrab gpo ON gpo.gpotrabajo = a.cvegpot " +
                    "         LEFT JOIN admgcn.r3orden o ON o.numorden = a.numorden " +
                    "         LEFT JOIN admgcn.r3equipo eq ON eq.equipo = o.equipo " +
                    "         LEFT JOIN admgcn.r3tags tag ON tag.ubicacion = a.tag " +
                    filtro +
                    " ORDER BY a.tag, a.fechapini, a.numorden, a.cveactiv;");

            // TODO: quitar valores estaticos
            q1.setQry(qry.toString());
            ResultSet rs = q1.getRegisters();
            while (rs.next()) {
                ap = new ActividadPenetracion();
                ap.setiDActividad(rs.getString(1));
                ap.setOrdenTrabajo(rs.getString(2));
                ap.setUbicacionTecnica(rs.getString(3));
                ap.setDenominacionEquipo(rs.getString("denom_equipo"));
                ap.setDescripcionActividad(rs.getString(5));
                ap.setClaveSistema(rs.getString(6));
                ap.setDivision(rs.getString(7));
                ap.setGrupoTrabajo(rs.getString(8));
                ap.setArea(rs.getString(9));
                ap.setIngResponsable(rs.getString(10));
                ap.setFechaPlanInicio(rs.getDate("fechapini"));
                ap.setFechaPlanFin(rs.getDate("fechapfin"));
                ap.setFechaRealInicio(rs.getDate("fecharini") );
                ap.setFechaRealFin(rs.getDate("fecharfin") );
                ap.setPorcentajeAvance(rs.getDouble("porcentaje"));

                ap.setComponenteTag(rs.getString("cvecomp") );
                ap.setComponenteTag(rs.getString("consectag"));

                String duracionOriginal = rs.getString("dur_original");
                ap.setDuracionOriginal(obtenerDuracionOriginal(duracionOriginal));

                lista.add(ap);
            }
        } catch (Exception e) {
            Utilerias.grabarArchivo(Utilerias.getRutaEjecucion() + "bitacora.err", "PVP - " + e.getMessage(), true);
            throw new DAOException(e);
        } finally {
            q1.desconectarBD();
        }
        return lista;
    }

    // Recibe la resta entre fechapini y fechapfin en el formato DD HH:MM:SS y lo convierte a entero
    private Double obtenerDuracionOriginal(String duracion) {
        String pattern = "(\\d+)\\s(\\d+):\\d+";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(duracion);
        if (m.find()) {
            Double dias = Double.parseDouble(m.group(1));
            Double horas = Double.parseDouble(m.group(2));

            if (horas > 0)
                return Double.valueOf(dias + ((horas * 100) / 24) / 100);
            else
                return Double.valueOf(dias);
        }
        return 0d;
    }


    public List<RelacionPenetValvulas> obtenerRelaciones() {

        Informix q1 = new Informix("Cavam", "Informix/Cavam");
        List<RelacionPenetValvulas> lista = new ArrayList<RelacionPenetValvulas>();
        try {

            String qry = "SELECT TRIM(ubicacion), TRIM(penetracion) FROM pvp_ubicpenet; ";
            q1.setQry(qry);
            ResultSet rs = q1.getRegisters();
            while (rs.next()) {
                RelacionPenetValvulas rel = new RelacionPenetValvulas();
                rel.setValRelacionada(rs.getString(1));
                rel.setValPenet(rs.getString(2));

                lista.add(rel);
            }
        } catch (Exception e) {
            throw new DAOException(e);
        } finally {
            q1.desconectarBD();
        }
        return lista;

    }


    public List<String> obtenerDenominacionEquipos(String ubicacion){
        Informix q1 = new Informix("Cavam", "Informix/Cavam");
        List<String> lista = new ArrayList<String>();
        try {

            String qry = "SELECT TRIM(denequipo) " +
                    "FROM r3eqinst e " +
                    "         LEFT JOIN r3equipo a ON a.equipo = e.equipo " +
                    "WHERE e.ubicacion = '"+ubicacion+"' " +
                    "  AND fecret = '31/12/9999'; ";
            q1.setQry(qry);
            ResultSet rs = q1.getRegisters();
            while (rs.next()) {
              lista.add(rs.getString(1));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        } finally {
            q1.desconectarBD();
        }
        return lista;


    }


}
