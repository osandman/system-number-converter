public class Run {
    public static void main(String[] args) {
        SystemNumberConverter converter = new SystemNumberConverter();
//        converter.setSysX(2);
//        converter.setSysY(10);
        System.out.println(converter.getSysX());
        System.out.println(converter.getSysY());
//        converter.runConverter("11");
//        converter.runConverter(10, 2, "16");
        converter.runConverter();
    }
}
