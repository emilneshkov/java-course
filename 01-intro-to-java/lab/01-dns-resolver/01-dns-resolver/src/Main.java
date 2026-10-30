void main() {
    String[] from = {"moodle.bg", "login.moodle.bg"};
    String[] to= {"login.moodle.bg", "moodle.bg"};

    String result = DnsResolver.resolve(from, to, "moodel.bg");
    System.out.println(result);
}
