package br.com.android.sample.infrastructure.calcular;

/**
 * Created by tony on 01/10/16.
 */
public class Calcular {

    private static final int CORRECAO_ANGULO = 88;
    private static final double CORRECAO_NORTE_VERDADEIRO = -16;

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

        eixoY =  eixoY + CORRECAO_ANGULO + (int) CORRECAO_NORTE_VERDADEIRO;
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

        eixoY = eixoY + CORRECAO_ANGULO + CORRECAO_NORTE_VERDADEIRO;
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

    /*private static final double tg[] = new double[]{0.0, 0.0174550649, 0.0349207695, 0.0524077793, 0.0699268119,
            0.0874886635, 0.1051042353, 0.1227845609, 0.1405408347, 0.1583844403, 0.1763269807, 0.1943803091,
            0.2125565617, 0.2308681911, 0.2493280028, 0.2679491924, 0.2867453858, 0.3057306815, 0.3249196962,
            0.3443276133, 0.3639702343, 0.383864035, 0.4040262258, 0.4244748162, 0.4452286853, 0.4663076582,
            0.4877325886, 0.5095254495, 0.5317094317, 0.5543090515, 0.5773502692, 0.600860619, 0.6248693519,
            0.6494075932, 0.6745085168, 0.7002075382, 0.726542528, 0.7535540501, 0.7812856265, 0.8097840332,
            0.8390996312, 0.8692867378, 0.9004040443, 0.9325150861, 0.9656887748, 1.0, 1.0355303138, 1.07236871,
            1.1106125148, 1.1503684072, 1.1917535926, 1.2348971565, 1.2799416322, 1.3270448216, 1.3763819205,
            1.4281480067, 1.4825609685, 1.5398649638, 1.600334529, 1.6642794824, 1.7320508076, 1.8040477553,
            1.8807264653, 1.9626105055, 2.0503038416, 2.1445069205, 2.2460367739, 2.3558523658, 2.4750868534,
            2.6050890647, 2.7474774195, 2.9042108777, 3.0776835372, 3.2708526185, 3.4874144438, 3.7320508076,
            4.0107809335, 4.3314758743, 4.7046301095, 5.144554016, 5.6712818196, 6.3137515147, 7.1153697224,
            8.144346428, 9.5143644542, 11.4300523028, 14.3006662567, 19.0811366877, 28.6362532829, 57.2899616308};

    private static final double cos[] = new double[]{1.0, 0.9998476952, 0.999390827, 0.9986295348, 0.9975640503,
            0.9961946981, 0.9945218954, 0.9925461516, 0.9902680687, 0.9876883406, 0.984807753, 0.9816271834,
            0.9781476007, 0.9743700648, 0.9702957263, 0.9659258263, 0.9612616959, 0.956304756, 0.9510565163,
            0.9455185756, 0.9396926208, 0.9335804265, 0.9271838546, 0.9205048535, 0.9135454576, 0.906307787,
            0.8987940463, 0.8910065242, 0.8829475929, 0.8746197071, 0.8660254038, 0.8571673007, 0.8480480962,
            0.8386705679, 0.8290375726, 0.8191520443, 0.8090169944, 0.79863551, 0.7880107536, 0.7771459615,
            0.7660444431, 0.7547095802, 0.7431448255, 0.7313537016, 0.7193398003, 0.7071067812, 0.6946583705,
            0.6819983601, 0.6691306064, 0.656059029, 0.6427876097, 0.629320391, 0.6156614753, 0.6018150232,
            0.5877852523, 0.5735764364, 0.5591929035, 0.544639035, 0.5299192642, 0.5150380749, 0.5, 0.4848096202,
            0.4694715628, 0.4539904997, 0.4383711468, 0.4226182617, 0.4067366431, 0.3907311285, 0.3746065934,
            0.3583679495, 0.3420201433, 0.3255681545, 0.3090169944, 0.2923717047, 0.2756373558, 0.2588190451,
            0.2419218956, 0.2249510543, 0.2079116908, 0.1908089954, 0.1736481777, 0.156434465, 0.139173101,
            0.1218693434, 0.1045284633, 0.0871557427, 0.0697564737, 0.0523359562, 0.0348994967, 0.0174524064};*/
}