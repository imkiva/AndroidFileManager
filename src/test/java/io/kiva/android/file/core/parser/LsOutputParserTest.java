package io.kiva.android.file.core.parser;

import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * @author kiva
 * @date 2018/2/14
 */
public class LsOutputParserTest {
    private static final String[] TEXT = new String[]{
            "dr-xr-xr-x root     root              2018-02-08 12:50 acct",
            "drwxrwx--- system   cache             2017-12-16 09:33 cache",
            "lrwxrwxrwx root     root              1970-01-01 08:00 charger -> /sbin/healthd",
            "lrwxrwxrwx root     root              1970-01-01 08:00 charger.bin -> /sbin/healthd.bin",
            "dr-x------ root     root              2018-02-08 12:50 config",
            "drwxr-xr-x root     root              2017-06-18 00:08 custom",
            "lrwxrwxrwx root     root              2018-02-08 12:50 d -> /sys/kernel/debug",
            "drwxrwx--x system   system            2018-02-14 10:55 data",
            "-rw-r--r-- root     root          707 1970-01-01 08:00 default.prop",
            "drwxr-xr-x root     root              2018-02-08 12:50 dev",
            "-rw-r--r-- root     root          127 1970-01-01 08:00 enableswap.sh",
            "lrwxrwxrwx root     root              2018-02-08 12:50 etc -> /system/etc",
            "-rw-r--r-- root     root         2741 1970-01-01 08:00 factory_init.project.rc",
            "-rw-r--r-- root     root        19916 1970-01-01 08:00 factory_init.rc",
            "-rw-r--r-- root     root        47183 1970-01-01 08:00 file_contexts",
            "-rw-r----- root     root         2959 1970-01-01 08:00 fstab.mt6755",
            "-rwxr-x--- root     root      1158992 1970-01-01 08:00 init",
            "-rwxr-x--- root     root          673 1970-01-01 08:00 init.aee.rc",
            "-rwxr-x--- root     root         2065 1970-01-01 08:00 init.c2k.rc",
            "-rwxr-x--- root     root          430 1970-01-01 08:00 init.common_svc.rc",
            "-rwxr-x--- root     root         1293 1970-01-01 08:00 init.environ.rc",
            "-rwxr-x--- root     root         1002 1970-01-01 08:00 init.mal.rc",
            "-rwxr-x--- root     root         1292 1970-01-01 08:00 init.microtrust.rc",
            "-rwxr-x--- root     root         5598 1970-01-01 08:00 init.modem.rc",
            "-rwxr-x--- root     root        74677 1970-01-01 08:00 init.mt6755.rc",
            "-rwxr-x--- root     root        50687 1970-01-01 08:00 init.mt6755.usb.rc",
            "-rwxr-x--- root     root         8432 1970-01-01 08:00 init.project.rc",
            "-rwxr-x--- root     root        28153 1970-01-01 08:00 init.rc",
            "-rwxr-x--- root     root          972 1970-01-01 08:00 init.recovery.mt6755.rc",
            "-rwxr-x--- root     root         2555 1970-01-01 08:00 init.trace.rc",
            "-rwxr-x--- root     root         3885 1970-01-01 08:00 init.usb.rc",
            "-rwxr-x--- root     root         1172 1970-01-01 08:00 init.volte.rc",
            "-rwxr-x--- root     root          583 1970-01-01 08:00 init.xlog.rc",
            "-rwxr-x--- root     root          301 1970-01-01 08:00 init.zygote32.rc",
            "-rwxr-x--- root     root          531 1970-01-01 08:00 init.zygote64_32.rc",
            "-rw-r--r-- root     root         1004 1970-01-01 08:00 meta_init.c2k.rc",
            "-rw-r--r-- root     root         1290 1970-01-01 08:00 meta_init.modem.rc",
            "-rw-r--r-- root     root          701 1970-01-01 08:00 meta_init.project.rc",
            "-rw-r--r-- root     root        14657 1970-01-01 08:00 meta_init.rc",
            "drwxr-xr-x root     system            2018-02-08 12:50 mnt",
            "drwxrwx--x root     system            2010-01-01 08:00 nvdata",
            "drwxr-xr-x root     root              1970-01-01 08:00 oem",
            "dr-xr-xr-x root     root              1970-01-01 08:00 proc",
            "-rw-r--r-- root     root        12984 1970-01-01 08:00 property_contexts",
            "drwxrwx--- system   system            2010-01-01 08:00 protect_f",
            "drwxrwx--- system   system            2010-01-01 08:00 protect_s",
            "drwxr-xr-x root     root              1970-01-01 08:00 res",
            "drwx------ root     root              2017-05-26 22:36 root",
            "drwxr-x--- root     root              1970-01-01 08:00 sbin",
            "lrwxrwxrwx root     root              2018-02-08 12:50 sdcard -> /storage/self/primary",
            "-rw-r--r-- root     root          673 1970-01-01 08:00 seapp_contexts",
            "-rw-r--r-- root     root           80 1970-01-01 08:00 selinux_version",
            "-rw-r--r-- root     root       345830 1970-01-01 08:00 sepolicy",
            "-rw-r--r-- root     root        14895 1970-01-01 08:00 service_contexts",
            "drwxr-xr-x root     root              2018-02-08 12:50 storage",
            "dr-xr-xr-x root     root              2018-02-08 12:50 sys",
            "drwxr-xr-x root     root              2017-06-18 00:44 system",
            "drwxr-xr-x root     root              2018-02-08 12:50 tmp-mksh",
            "-rw-r--r-- root     root         5240 1970-01-01 08:00 ueventd.mt6755.rc",
            "-rw-r--r-- root     root         4691 1970-01-01 08:00 ueventd.rc",
            "lrwxrwxrwx root     root              2018-02-08 12:50 vendor -> /system/vendor",
            "-rw-r--r-- root     root          524 1970-01-01 08:00 verity_key",
    };

    @Test
    public void parse() {
        Pattern pattern = LsOutputParser.PATTERN;

        Arrays.stream(TEXT)
                .forEach(it -> {
                    System.out.println(it);
                    Matcher matcher = pattern.matcher(it);
                    assertTrue(matcher.matches());
                    for (int i = 0; i < matcher.groupCount(); ++i) {
                        String s = matcher.group(i);
                        System.out.println("Group #" + i + "\t: " + s);
                    }
                });
    }

    @Test
    public void regex() {
        Pattern pattern = LsOutputParser.PATTERN;

        Arrays.stream(TEXT)
                .forEach(it -> {
                    System.out.println(it);
                    assertTrue(pattern.matcher(it).matches());
                });
    }
}