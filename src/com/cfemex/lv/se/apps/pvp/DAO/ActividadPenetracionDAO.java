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
            qry.append("SELECT TRIM(a.cveactiv) as id_actividad, " +
                    "       TRIM(a.numorden) as numorden , " +
                    "       TRIM(a.tag)  as ubicacion, " +
                    "       TRIM(eq.denequipo)  AS denom_equipo, " +
                    "       TRIM(a.descactiv) as descactiv, " +
                    "       TRIM(a.sist) AS sistema, " +
                    "       TRIM(a.div)  AS division, " +
                    "       TRIM(gpo.descripcion) as desc_grupo, " +
                    "       TRIM(a.area) as area, " +
                    "       TRIM(a.cveingresp) as ing_responsable, " +
                    "       a.fechapini, " +
                    "       a.fechapfin, " +
                    "       a.fecharini, " +
                    "       a.fecharfin, " +
                    "       a.porcentaje, " +
                    "       (a.fechapfin -  a.fechapini) as dur_original, " +
                    // Si la OT tiene ubicacion tomamos el cuarto, nivel y edificio de la ot, si no tomamos los valores de la Actividad
                    "       CASE WHEN tag_ot.ubicacion IS NOT NULL THEN tag_ot.cuarto ELSE tag_activ.cuarto END AS cuarto, " +
                    "       CASE WHEN tag_ot.ubicacion IS NOT NULL THEN tag_ot.cveedif ELSE tag_activ.cveedif END AS edificio, " +
                    "       CASE WHEN tag_ot.ubicacion IS NOT NULL THEN tag_ot.cveniv ELSE tag_activ.cveniv END AS nivel " +
                    " FROM admgcn.r3activ a " +
                    "       LEFT JOIN admgcn.planegpotrab gpo ON gpo.gpotrabajo = a.cvegpot " +
                    "       LEFT JOIN admgcn.r3orden o ON o.numorden = a.numorden " +
                    "       LEFT JOIN admgcn.r3equipo eq ON eq.equipo = o.equipo " +
                    "       LEFT JOIN admgcn.r3tags tag_activ ON tag_activ.ubicacion = a.tag " +
                    "       LEFT JOIN admgcn.r3tags tag_ot ON tag_ot.ubicacion = o.ubicacion" +
                    filtro +
                    " ORDER BY a.tag, a.fechapini, a.numorden, a.cveactiv;");

            // TODO: quitar valores estaticos
            q1.setQry(qry.toString());
            ResultSet rs = q1.getRegisters();
            while (rs.next()) {
                ap = new ActividadPenetracion();
                ap.setiDActividad(rs.getString("id_actividad"));
                ap.setOrdenTrabajo(rs.getString("numorden"));
                ap.setUbicacionTecnica(rs.getString("ubicacion"));
                ap.setDenominacionEquipo(rs.getString("denom_equipo"));
                ap.setDescripcionActividad(rs.getString("descactiv"));
                ap.setClaveSistema(rs.getString("sistema"));
                ap.setDivision(rs.getString("division"));
                ap.setGrupoTrabajo(rs.getString("desc_grupo"));
                ap.setArea(rs.getString("area"));
                ap.setIngResponsable(rs.getString("ing_responsable"));
                ap.setFechaPlanInicio(rs.getDate("fechapini"));
                ap.setFechaPlanFin(rs.getDate("fechapfin"));
                ap.setFechaRealInicio(rs.getDate("fecharini"));
                ap.setFechaRealFin(rs.getDate("fecharfin"));
                ap.setPorcentajeAvance(rs.getDouble("porcentaje"));
                ap.setCuarto(rs.getString("cuarto"));
                ap.setEdificio(rs.getString("edificio"));
                ap.setNivel(rs.getString("nivel"));

                String duracionOriginal = rs.getString("dur_original");
                ap.setDuracionOriginal(obtenerDuracionOriginal(duracionOriginal));

                lista.add(ap);
            }
        } catch (Exception e) {
            Utilerias.grabarArchivo(Utilerias.getRutaEjecucion() + "bitacora.err", "PVP - " + e.toString(), true);
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


    public List<String> obtenerDenominacionEquipos(String ubicacion) {
        Informix q1 = new Informix("Cavam", "Informix/Cavam");
        List<String> lista = new ArrayList<String>();
        try {

            String qry = "SELECT TRIM(denequipo) " +
                    "FROM r3eqinst e " +
                    "         LEFT JOIN r3equipo a ON a.equipo = e.equipo " +
                    "WHERE e.ubicacion = '" + ubicacion + "' " +
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
