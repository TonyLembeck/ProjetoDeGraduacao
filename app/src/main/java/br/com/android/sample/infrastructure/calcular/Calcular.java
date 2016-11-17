package br.com.android.sample.infrastructure.calcular;

/**
 * Created by tony on 01/10/16.
 */
public class Calcular {

    private static final int CORRECAO_ANGULO = 90;

    public static double distancia(double altura, double eixoZ) {

        return Math.tan((Math.PI /180 ) * eixoZ) * altura;
    }

    public static double altura(double alturaUsuario, double distancia, double eixoZ, double eixoX) {

        if (eixoX > 0) {
            return alturaUsuario - (Math.tan((Math.PI /180 ) * (90 - eixoZ)) * distancia);
        } else
            return (Math.tan((Math.PI /180 ) * (90 - eixoZ)) * distancia) + alturaUsuario;
    }

    public static double latitude(double distancia, double eixoY, double latitude) {

        eixoY =  eixoY + CORRECAO_ANGULO;
        if (eixoY > 359)
            eixoY = eixoY - 360;

        if (eixoY < 90)
            return latitude + getGrausDD(Math.cos((Math.PI /180 ) * eixoY) * distancia);

        else if (eixoY > 90 && eixoY < 180)
            return latitude - getGrausDD(Math.cos((Math.PI /180 ) * (180 - eixoY)) * distancia);

        else if (eixoY > 180 && eixoY < 270)
            return latitude - getGrausDD(Math.cos((Math.PI /180 ) * (eixoY - 180)) * distancia);

        else if (eixoY > 270 && eixoY < 360)
            return latitude + getGrausDD(Math.cos((Math.PI /180 ) * (360 - eixoY)) * distancia);

        else if (eixoY == 0)
            return latitude + getGrausDD(distancia);

        else if (eixoY == 90 || eixoY == 270)
            return latitude;

        else if (eixoY == 180)
            return latitude - getGrausDD(distancia);

        return 0;
    }

    public static double longitude(double distancia, double eixoY, double longitude) {

        eixoY = eixoY + CORRECAO_ANGULO;
        if (eixoY > 359)
            eixoY = eixoY - 360;

        if (eixoY > 0 && eixoY < 90)
            return longitude + getGrausDD(Math.cos((Math.PI /180 ) * (90 - eixoY))* distancia);

        else if (eixoY > 90 && eixoY < 180)
            return longitude + getGrausDD(Math.cos((Math.PI /180 ) * (eixoY - 90)) * distancia);

        else if (eixoY > 180 && eixoY < 270)
            return longitude - getGrausDD(Math.cos((Math.PI /180 ) * (270 - eixoY)) * distancia);

        else if (eixoY > 270 && eixoY < 360)
            return longitude - getGrausDD(Math.cos((Math.PI /180 ) * (eixoY - 270)) * distancia);

        else if (eixoY == 90)
            return longitude + getGrausDD(distancia);

        else if (eixoY == 0 || eixoY == 180)
            return longitude;

        else if (eixoY == 270)
            return longitude - getGrausDD(distancia);

        return 0;
    }

    private static double getGrausDD(double distancia) {

        return ((distancia / 1852) / 60);
    }
}