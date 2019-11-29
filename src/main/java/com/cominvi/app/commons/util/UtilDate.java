package com.cominvi.app.commons.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Clase que se encarga de tener todas las utilidades para minupular las fechas.
 * Hay metodos que son estaticos y otros que no
 */
public class UtilDate {

    public static final String FORMATSTANDARSQL = "yyyyMMdd";
    public static final String FORMAT_STANDAR_DATE_ANDROID = "yyyy-MM-dd";
    public static final String FORMAT_STANDAR_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_EXCEL = "m-d-yy h:mm";
    public static final String FORMAT_DATE_WITH_HR_MIN = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_STANDAR_DATE_WITH_HR_MIN = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_STANDAR_DATE_WITH_HR_MIN_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_STANDAR_DATE_WITH_HR_MIN_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String FORMAT_TIMEZONE_CST = "GMT";

    public static final String FORMAT_DATE_MES_ANIO = "MMM YYYY";

    private UtilDate() {
    }

    /**
     * recupera la fecha actual pero en milisegundos.
     *
     * @return la fecha actual en milisegundos
     */
    public static long getDateMili() {
        return new Date().getTime();
    }

    /**
     * Convierte una fecha en el formato especificado
     *
     * @param time   fecha de tipo date
     * @param format formato en que se quiere convertir la fecha
     * @return regresa la fecha en un string con el formato deseado
     * @throws NullPointerException     si no puede convertir la fecha por venir null
     * @throws IllegalArgumentException si el formato no es el correcto o no puede convertirlo
     */
    public static String getFormatByFecha(Date time, String format) throws NullPointerException, IllegalArgumentException {
        String text = "";
        DateFormat df = new SimpleDateFormat(format);
        text = df.format(time);
        return text;
    }

    /**
     * Apartir de un año y un mes en enteros se genera un date tener en cuenta que si anio es cero tomara la fecha actual si el mes es cero tomara el mes actual
     * <p>
     * para los meses se toman apartir de 1 como enero
     * <p>
     * este no contemplea las horas, minutos, segundos y milisegundos estos los toma de la fecha actual.
     *
     * @param anioOrg año en el que se trabaja
     * @param mesOrg  mes en el que se trabaja
     * @return devuelve la fecha los primeros de campo
     */
    public static Date convertDate(Integer anioOrg, Integer mesOrg) {
        Integer anio = anioOrg;
        Integer mes = mesOrg;
        Calendar c = new GregorianCalendar();
        if (anio == 0) {
            anio = c.get(Calendar.YEAR);
        }
        if (mes == 0) {
            mes = c.get(Calendar.MONTH) + 1;
        }
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, mes - 1);
        c.set(Calendar.YEAR, anio);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }


    /**
     * Para sacar el primer día de la fecha que se le mande, tomar en cuenta que la hora tambien sera con las horas, minutos y segurndos en cero.
     *
     * @param fecha fecha que se quiere saber el primer dia del mes
     * @return un date con el primer día del mes
     */
    public static Date getDatePrimerDiaMes(Date fecha) {
        Date fechaFinal = null;
        if (fecha != null) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(fecha);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getMinimum(Calendar.HOUR_OF_DAY), cal.getMinimum(Calendar.MINUTE),
                    cal.getMinimum(Calendar.SECOND));
            fechaFinal = cal.getTime();
        }
        return fechaFinal;
    }

    /**
     * Para sacar el último día de la fecha que se le mande, tomar en cuenta que la hora tambien sera con las horas, minutos y segurndos seran con el último segundo del día.
     *
     * @param fecha fecha que se quiere saber el último dia del mes
     * @return un date con el último día del mes
     */
    public static Date getDateUltimoDiaMes(Date fecha) {
        Date fechaFinal = null;
        if (fecha != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH), cal.getMaximum(Calendar.HOUR_OF_DAY), cal.getMaximum(Calendar.MINUTE),
                    cal.getMaximum(Calendar.SECOND));
            fechaFinal = cal.getTime();
        }
        return fechaFinal;
    }

    /**
     * Para sacar el último día de la fecha que se le mande, tomar en cuenta que la hora tambien sera con las horas, minutos y segurndos en ceros.
     *
     * @param fecha fecha que se quiere saber el último dia del mes
     * @return un date con el último día del mes
     */
    public static Date getDateUltimoDiaMesHorasCero(Date fecha) {
        Calendar cal = Calendar.getInstance();
        Date fechaFinal = null;
        if (fecha != null) {
            cal.setTime(fecha);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH), cal.getMinimum(Calendar.HOUR_OF_DAY), cal.getMinimum(Calendar.MINUTE),
                    cal.getMinimum(Calendar.SECOND));
            fechaFinal = cal.getTime();
        }
        return fechaFinal;
    }

    /**
     * Genera una fecha con el mes y anio, tomar en cuenta que el dia, hora, minutos y segundos se van a generar apartir del momento que se hizo la solicitud
     *
     * @param mes  con el que se inicia la fecha
     * @param anio con el que se inicia la fecha
     * @return regresa la fecha con los valores minimos del dia, hora, minuto, segundo y milisegundos
     */
    public static Date getDateByMesAnio(Integer mes, Integer anio) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, cal.getMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    /**
     * Apartir de una lista de fechas devuelve la primera lista
     *
     * @param listDates
     * @return
     */
    public static Date getFirstDate(List<Date> listDates) {
        Date fechaInicial = null;
        for (Date date : listDates) {
            if (fechaInicial != null) {
                fechaInicial = valDate(fechaInicial, date);
            } else {
                if (date != null) {
                    fechaInicial = date;
                }
            }
        }
        return fechaInicial;
    }

    /**
     * Valida 2 fechas y regresa la fecha que es primero de las 2
     *
     * @param fechaInicial fecha 1 que se quiere comparar
     * @param date         fecha 2 que se quiere comparar
     * @return la fecha que sea menor de las 2
     */
    private static Date valDate(Date fechaInicial, Date date) {
        Date fecha = fechaInicial;
        if (date != null && date.before(fechaInicial)) {
            fecha = date;
        }
        return fecha;
    }

    /**
     * Entre 2 fechas se desea hacer una lista consecutivas por meses ejemplo:
     * 18/01/2018 12/03/2018
     * <p>
     * 18/01/2018
     * 18/02/2018
     * 12/03/2018
     *
     * @param fechaInicial fecha inicial donde empezara el recorrido
     * @param fechaFinal   fecha final donde se detendra el recorrido
     * @return una lista de fechas que estan en ese rango separa por 1 mes
     */
    public static List<Date> getConsecutivoMes(Date fechaInicial, Date fechaFinal) {
        List<Date> fechas = new ArrayList<>();
        if (fechaInicial != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fechaInicial);
            while (calendar.getTime().before(fechaFinal)) {
                Date result = calendar.getTime();
                fechas.add(result);
                calendar.add(Calendar.MONTH, 1);
            }
            fechas.add(fechaFinal);
        } else {
            fechas.add(fechaFinal);
        }
        return fechas;
    }

    /**
     * Regresa la cantidad de meses que exiten entre 2 fechas
     * @param fechaInicial fecha inicial a comparar
     * @param fechaFinal fecha final a comparar
     * @return la cantidad de meses que existe entre 2 meses
     */
    public static Integer getCountEntreMes(Date fechaInicial, Date fechaFinal) {
        Integer count = 0;
        if (fechaInicial != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fechaInicial);
            while (calendar.getTime().before(fechaFinal)) {
                count++;
                calendar.add(Calendar.MONTH, 1);
            }
        }
        return count;
    }

    /**
     * Una forma rapido de recuperar la fecha actual en un formato yyyy-MM-dd HH:mm
     * @return string con la fecha actual
     */
    public static String getFechaActual() {
        Calendar date = GregorianCalendar.getInstance();
        return UtilDate.getFormatByFecha(date.getTime(), UtilDate.FORMAT_DATE_WITH_HR_MIN);
    }

    /**
     * Convierte un string que contiene una fecha en un formato establecido a un objeto date
     *
     * @param dateInString      la fecha en formato string
     * @param formatStandarDate el formato en que se desea parsear la fecha
     * @return regresa un date de los parametros enviados
     * @throws ParseException si no puede convertir el string al formato
     */
    public static Date formatStringToDate(String dateInString, String formatStandarDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStandarDate);
        return formatter.parse(dateInString);
    }

    /**
     * Hay busquedas en donde se requiere la hora,minuto y segundos finales del día. el año,mes y día no lo cambia
     *
     * @param fecha que se quiere cambiar
     * @return la fecha con la ultima hora, min y segundo del día.
     */
    public static Date getDateUtilmaHoraMinSec(Date fecha) {
        Date fechaFinal = null;
        if (fecha != null) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(fecha);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.getMaximum(Calendar.HOUR_OF_DAY), cal.getMaximum(Calendar.MINUTE), cal.getMaximum(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
            fechaFinal = cal.getTime();
        }
        return fechaFinal;
    }

    /**
     * Hay busquedas en donde se requiere la hora,minuto y segundos iniciales del día. el año,mes y día no lo cambia
     *
     * @param fecha que se quiere cambiar
     * @return la fecha con la ultima hora, min y segundo del día.
     */
    public static Date getDateInicialHoraMinSec(Date fecha) {
        Date fechaFinal = null;
        if (fecha != null) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(fecha);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.getMinimum(Calendar.HOUR_OF_DAY), cal.getMinimum(Calendar.MINUTE), cal.getMinimum(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
            fechaFinal = cal.getTime();
        }
        return fechaFinal;
    }

    /**
     * Busca entre 2 fechas los días de la semana el día de la semana se maneja como Calendar
     *
     * @param fechaI     fecha inicial
     * @param fechaF     fecha final
     * @param daysOfWeek día de la semana Calendar.SUNDAY
     * @return los días de la semana que encontraron en el rango
     */
    public static List<Date> getDiasSemana(Date fechaI, Date fechaF, int... daysOfWeek) {
        List<Date> domingos = new ArrayList<>();
        Calendar fechaInicial = GregorianCalendar.getInstance();
        fechaInicial.setTime(fechaI);
        Calendar fechaFinal = GregorianCalendar.getInstance();
        fechaFinal.setTime(fechaF);
        while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {
            for (int i : daysOfWeek) {
                if (fechaInicial.get(Calendar.DAY_OF_WEEK) == i) {
                    domingos.add(fechaInicial.getTime());
                }
            }
            fechaInicial.add(Calendar.DATE, 1);
        }
        return domingos;
    }

    /**
     * para sacar la diferencia con la cantidad de horas entre mexico y UTC
     *
     * @return el numero de horas este valor sera -5 y -6 dependiendo del horario de verano
     */
    public static int getDiferenciaMexicoCityAndUTC() {
        return getDiferenceTimesZones("America/Mexico_City", "Etc/UTC");
    }

    /**
     * metodo para sacar la diferencia de horas entre 2 zonas horario
     *
     * @param zone1 zona principal
     * @param zone2 zona con la que se quiere sacar la diferencia
     * @return el numero de horas que hay entre las 2 zonas
     */
    public static int getDiferenceTimesZones(String zone1, String zone2) {
        return TimeZone.getTimeZone(zone1).getRawOffset() - TimeZone.getTimeZone(zone2).getRawOffset();
    }

}
