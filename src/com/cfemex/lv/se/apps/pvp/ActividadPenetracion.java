package com.cfemex.lv.se.apps.pvp;


import java.util.Date;
import java.util.List;

public class ActividadPenetracion {

    private String                     iDActividad;
    private String                     ordenTrabajo;
    private String                     ubicacionTecnica;
    private String                     denominacionEquipo;
    private String                     descripcionActividad;
    private String                     claveSistema;
    private String                     division;
    private String                     tipoMantto;
    private String                     grupoTrabajo;
    private String                     area;
    private String                     ingResponsable;
    private Date                       fechaPlanInicio;
    private Date                       fechaPlanFin;
    private Date                       fechaRealInicio;
    private Date                       fechaRealFin;
    private Double                     duracionOriginal;
    private List<ActividadPenetracion> actividadesRelacionadas;
    private List<String>               equipos;
    private Double                     porcentajeAvance;
    private String                     cuarto;
    private String                     edificio;
    private String                     nivel; // Tambien se le llama Elevación

    public String getiDActividad() {
        return iDActividad;
    }

    public void setiDActividad(String iDActividad) {
        this.iDActividad = iDActividad;
    }

    public String getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(String ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public String getUbicacionTecnica() {
        return ubicacionTecnica;
    }

    public void setUbicacionTecnica(String ubicacionTecnica) {
        this.ubicacionTecnica = ubicacionTecnica;
    }

    public String getDenominacionEquipo() {
        return denominacionEquipo;
    }

    public void setDenominacionEquipo(String denominacionEquipo) {
        this.denominacionEquipo = denominacionEquipo;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public String getClaveSistema() {
        return claveSistema;
    }

    public void setClaveSistema(String claveSistema) {
        this.claveSistema = claveSistema;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getTipoMantto() {
        return tipoMantto;
    }

    public void setTipoMantto(String tipoMantto) {
        this.tipoMantto = tipoMantto;
    }

    public String getGrupoTrabajo() {
        return grupoTrabajo;
    }

    public void setGrupoTrabajo(String grupoTrabajo) {
        this.grupoTrabajo = grupoTrabajo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIngResponsable() {
        return ingResponsable;
    }

    public void setIngResponsable(String ingResponsable) {
        this.ingResponsable = ingResponsable;
    }

    public Date getFechaPlanInicio() {
        return fechaPlanInicio;
    }

    public void setFechaPlanInicio(Date fechaPlanInicio) {
        this.fechaPlanInicio = fechaPlanInicio;
    }

    public Date getFechaPlanFin() {
        return fechaPlanFin;
    }

    public void setFechaPlanFin(Date fechaPlanFin) {
        this.fechaPlanFin = fechaPlanFin;
    }

    public Date getFechaRealInicio() {
        return fechaRealInicio;
    }

    public void setFechaRealInicio(Date fechaRealInicio) {
        this.fechaRealInicio = fechaRealInicio;
    }

    public Date getFechaRealFin() {
        return fechaRealFin;
    }

    public void setFechaRealFin(Date fechaRealFin) {
        this.fechaRealFin = fechaRealFin;
    }

    public Double getDuracionOriginal() {
        return duracionOriginal;
    }

    public void setDuracionOriginal(Double duracionOriginal) {
        this.duracionOriginal = duracionOriginal;
    }

    public List<ActividadPenetracion> getActividadesRelacionadas() {
        return actividadesRelacionadas;
    }

    public void setActividadesRelacionadas(List<ActividadPenetracion> actividadesRelacionadas) {
        this.actividadesRelacionadas = actividadesRelacionadas;
    }

    public List<String> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<String> equipos) {
        this.equipos = equipos;
    }

    public Double getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(Double porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public String getCuarto() {
        return cuarto;
    }

    public void setCuarto(String cuarto) {
        this.cuarto = cuarto;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActividadPenetracion)) return false;
        ActividadPenetracion that = (ActividadPenetracion) o;
        if (getUbicacionTecnica() == null || that.getUbicacionTecnica() == null)
            return false;
        return getUbicacionTecnica().trim().equals(that.getUbicacionTecnica().trim());
    }

    @Override
    public int hashCode() {
        return getUbicacionTecnica().hashCode();
    }


    @Override
    public String toString() {
        return "ActividadPenetracion{" +
                "ubicacionTecnica='" + ubicacionTecnica + '\'' +
                '}';
    }
}
