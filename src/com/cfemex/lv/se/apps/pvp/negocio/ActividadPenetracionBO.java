package com.cfemex.lv.se.apps.pvp.negocio;

import com.cfemex.lv.se.apps.cavam.Recarga;
import com.cfemex.lv.se.apps.cavam.RecargaDAO;
import com.cfemex.lv.se.apps.pvp.ActividadPenetracion;
import com.cfemex.lv.se.apps.pvp.DAO.ActividadPenetracionDAO;
import com.cfemex.lv.se.apps.pvp.RelacionPenetValvulas;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class ActividadPenetracionBO implements IActividadPenetracionBO {

    public static ActividadPenetracionBO instance = null;

    public ActividadPenetracionBO() {

    }

    public static void main(String[] args) {
        getInstance().seleccionarActividadPenetracionCondicion(null);
    }

    public static ActividadPenetracionBO getInstance() {
        if (instance == null)
            instance = new ActividadPenetracionBO();
        return instance;
    }


    public List<ActividadPenetracion> seleccionarActividadPenetracionCondicion(List<String> condiciones) {
        List<RelacionPenetValvulas> listaRelaciones = ActividadPenetracionDAO.getInstance().obtenerRelaciones();
        List<ActividadPenetracion> listaActPenetraciones = obtenerActividadesPenetracion(condiciones);
        List<ActividadPenetracion> listaActRelacionadas = obtenerActividadesRelacionadas(condiciones, listaActPenetraciones);



        List<ActividadPenetracion> listaAux = new ArrayList<ActividadPenetracion>();

        for (ActividadPenetracion actPen : listaActPenetraciones) {

            // Si aun no esta en la list alo agregamos
            if (!listaAux.contains(actPen)) {
                listaAux.add(actPen);
            } else {
                // Si ya esta agregamos como hijo
                if (listaAux.get(listaAux.indexOf(actPen)).getActividadesRelacionadas() == null)
                    listaAux.get(listaAux.indexOf(actPen)).setActividadesRelacionadas(new ArrayList<ActividadPenetracion>());
                listaAux.get(listaAux.indexOf(actPen)).getActividadesRelacionadas().add(actPen);
            }
        }

        for (ActividadPenetracion actRel : listaActRelacionadas) {

            // Buscamos el padre en la lista
            ActividadPenetracion padre = getActividadPenetracionPadre(listaRelaciones, listaAux, actRel);
            // Si aun no esta en la list a lo agregamos
            if (padre.getActividadesRelacionadas() == null)
                padre.setActividadesRelacionadas(new ArrayList<ActividadPenetracion>());
            padre.getActividadesRelacionadas().add(actRel);

        }


        return listaAux;

    }

    private ActividadPenetracion getActividadPenetracionPadre(List<RelacionPenetValvulas> listaRelaciones, List<ActividadPenetracion> listaAux, ActividadPenetracion actRel) {
        String valvulaPadre = getPadre(actRel.getUbicacionTecnica(), listaRelaciones);
        ActividadPenetracion aux = new ActividadPenetracion();
        aux.setUbicacionTecnica(valvulaPadre);
        ActividadPenetracion padre = listaAux.get(listaAux.indexOf(aux));
        return padre;
    }

    public List<ActividadPenetracion> obtenerActividadesPenetracion(List<String> condiciones) {
        List<Recarga> recargas = RecargaDAO.getInstance().datosRecarga();

        if (recargas == null || recargas.size() == 0)
            return null;

        Recarga recarga = recargas.get(0);

        if (condiciones == null)
            condiciones = new ArrayList<String>();

        condiciones.add("(a.tag LIKE ('%-X-%') OR a.tag LIKE ('%-PENET-%'))");
        condiciones.add("a.unsist = " + recarga.getUnidadRecarga());
        condiciones.add("fechapini >=  to_date('" + recarga.getFechaProgramadaInicio() + "', '%Y-%m-%d')");
        condiciones.add("fechapfin <=  to_date('" + recarga.getFechaProgramadaFin() + "', '%Y-%m-%d')");
        condiciones.add("a.clasif = 'R' ");
        return ActividadPenetracionDAO.getInstance().seleccionarActividadPenetracion(condiciones);
    }

    public List<ActividadPenetracion> obtenerActividadesRelacionadas(List<String> condiciones, List<ActividadPenetracion> ubicacionesRelacionadas) {
        List<Recarga> recargas = RecargaDAO.getInstance().datosRecarga();

        if (recargas == null || recargas.size() == 0)
            return null;

        Recarga recarga = recargas.get(0);

        if (condiciones == null)
            condiciones = new ArrayList<String>();

        condiciones.add("a.unsist = " + recarga.getUnidadRecarga());
        condiciones.add("fechapini >=  to_date('" + recarga.getFechaProgramadaInicio() + "', '%Y-%m-%d')");
        condiciones.add("fechapfin <=  to_date('" + recarga.getFechaProgramadaFin() + "', '%Y-%m-%d')");
        condiciones.add("a.clasif = 'R' ");

        String ubicaciones = "";
        for (ActividadPenetracion act : ubicacionesRelacionadas) {
            ubicaciones += "'" + act.getUbicacionTecnica() + "',";
        }
        ubicaciones = ubicaciones.substring(0, ubicaciones.length() - 1);

        condiciones.add("a.tag in (select ubicacion from pvp_ubicpenet where penetracion in(" + ubicaciones + "))");
        return ActividadPenetracionDAO.getInstance().seleccionarActividadPenetracion(condiciones);
    }

    /*Para buscar la actividad de penetracion padre relacionada*/
    private String getPadre(String valvRel, List<RelacionPenetValvulas> listaRelaciones) {
        for (RelacionPenetValvulas rel : listaRelaciones) {
            if (valvRel.trim().equals(rel.getValRelacionada().trim()))
                return rel.getValPenet();
        }
        return null;
    }

    private String normalizeText(String text) {

        return text.toUpperCase().trim();
    }
}
