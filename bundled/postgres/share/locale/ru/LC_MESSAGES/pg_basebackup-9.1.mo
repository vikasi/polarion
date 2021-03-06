��    E      D  a   l      �     �       !     -   <     j  5   |  A   �  >   �  6   3  5   j  >   �  O   �  L   /  [   |  D   �  ,   	  7   J	  3   �	  5   �	  @   �	  /   -
  8   ]
  D   �
  Z   �
  P   6  4   �  @   �  (   �  -   &  "   T  #   w  .   �  (   �  #   �  9     &   Q  2   x  #   �  &   �     �           4  *   U  +   �  4   �  /   �  0     %   B  +   h     �  A   �  #   �  9     &   Q  -   x  !   �  (   �  "   �  ,        A  .   T  ,   �  4   �  %   �  "     
   .  &   9     `    h  ,   z      �  <   �  W        ]  P   y  G   �  t     S   �  R   �  R   .  s   �  l   �  �   b  m   �  ^   k  I   �  R     D   g  \   �  ;   	  p   E  �   �  �   �  �   u  c   V  �   �  7   E  F   }  9   �  F   �  U   E   ?   �   9   �   `   !  I   v!  e   �!  ]   &"  u   �"  A   �"  C   <#  @   �#  z   �#  P   <$  Y   �$  b   �$  W   J%  ;   �%  F   �%  3   %&  �   Y&  6   �&  `   '  H   z'  J   �'  0   (  A   ?(  4   �(  ]   �(  "   )  f   7)  V   �)  h   �)  D   ^*  F   �*     �*  [   �*     U+             
   =   4   D            	                   A   5       !   '   ;   )   8      %   (             B                      ?                    C                 :                    -       .                  <       0   $      9       &                1                 @            2   ,   6   7      "   *      >   E       +       3   /   #       
Connection options:
 
General options:
 
Options controlling the output:
 
Report bugs to <pgsql-bugs@postgresql.org>.
   %s [OPTION]...
   --help                   show this help, then exit
   --version                output version information, then exit
   -D, --pgdata=DIRECTORY   receive base backup into directory
   -F, --format=p|t         output format (plain, tar)
   -P, --progress           show progress information
   -U, --username=NAME      connect as specified database user
   -W, --password           force password prompt (should happen automatically)
   -Z, --compress=0-9       compress tar output with given compression level
   -c, --checkpoint=fast|spread
                           set fast or spread checkpointing
   -h, --host=HOSTNAME      database server host or socket directory
   -l, --label=LABEL        set backup label
   -p, --port=PORT          database server port number
   -v, --verbose            output verbose messages
   -w, --no-password        never prompt for password
   -x, --xlog               include required WAL files in backup
   -z, --gzip               compress tar output
 %s takes a base backup of a running PostgreSQL server.

 %s/%s kB (%d%%), %d/%d tablespace %s/%s kB (%d%%), %d/%d tablespaces %s/%s kB (%d%%), %d/%d tablespace (%-30.30s) %s/%s kB (%d%%), %d/%d tablespaces (%-30.30s) %s/%s kB (100%%), %d/%d tablespace %35s %s/%s kB (100%%), %d/%d tablespaces %35s %s: COPY stream ended before last file was finished
 %s: can only write single tablespace to stdout, database has %i
 %s: could not access directory "%s": %s
 %s: could not close compressed file "%s": %s
 %s: could not close file "%s": %s
 %s: could not connect to server: %s %s: could not create compressed file "%s": %s
 %s: could not create directory "%s": %s
 %s: could not create file "%s": %s
 %s: could not create symbolic link from "%s" to "%s": %s
 %s: could not get COPY data stream: %s %s: could not get WAL end position from server: %s %s: could not get backup header: %s %s: could not initiate base backup: %s %s: could not parse file mode
 %s: could not parse file size
 %s: could not read COPY data: %s %s: could not send base backup command: %s %s: could not set compression level %i: %s
 %s: could not set permissions on directory "%s": %s
 %s: could not set permissions on file "%s": %s
 %s: could not write to compressed file "%s": %s
 %s: could not write to file "%s": %s
 %s: directory "%s" exists but is not empty
 %s: final receive failed: %s %s: invalid checkpoint argument "%s", must be "fast" or "spread"
 %s: invalid compression level "%s"
 %s: invalid output format "%s", must be "plain" or "tar"
 %s: invalid tar block header size: %i
 %s: no WAL end position returned from server
 %s: no data returned from server
 %s: no start point returned from server
 %s: no target directory specified
 %s: only tar mode backups can be compressed
 %s: out of memory
 %s: received invalid directory (too long): %s
 %s: this build does not support compression
 %s: too many command-line arguments (first is "%s")
 %s: unrecognized link indicator "%c"
 %s: unsupported server version %s
 Password:  Try "%s --help" for more information.
 Usage:
 Project-Id-Version: PostgreSQL 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2014-04-02 02:14+0000
PO-Revision-Date: 2014-04-02 09:12+0400
Last-Translator: Alexander Lakhin <exclusion@gmail.com>
Language-Team: Russian <pgtranslation-translators@pgfoundry.org>
Language: ru
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=3; plural=n%10==1 && n%100!=11 ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2;
X-Generator: Lokalize 1.5
 
Параметры подключения:
 
Общие параметры:
 
Параметры, управляющие выводом:
 
Об ошибках сообщайте по адресу <pgsql-bugs@postgresql.org>.
   %s [ПАРАМЕТР]...
   --help                   показать эту справку и выйти
   --version                показать версию и выйти
   -D, --pgdata=КАТАЛОГ     сохранить базовую копию в указанный каталог
   -F, --format=p|t         формат вывода (p - простой, t - tar)
   -P, --progress           показывать прогресс операции
   -U, --username=ИМЯ       имя пользователя баз данных
   -W, --password           запрашивать пароль всегда (обычно не требуется)
   -Z, --compress=0-9       установить уровень сжатия выходного архива
   -c, --checkpoint=fast|spread
                           режим быстрых или распределённых контрольных точек
   -h, --host=ИМЯ           имя сервера баз данных или каталог сокетов
   -l, --label=МЕТКА        установить метку резервной копии
   -p, --port=ПОРТ          номер порта сервера БД
   -v, --verbose            выводить подробные сообщения
   -w, --no-password        не запрашивать пароль
   -x, --xlog               включить в копию требуемые файлы WAL
   -z, --gzip               сжать выходной tar
 %s делает базовую резервную копию работающего сервера PostgreSQL.

 %s/%s КБ (%d%%), табличное пространство %d/%d %s/%s КБ (%d%%), табличное пространство %d/%d %s/%s КБ (%d%%), табличное пространство %d/%d %s/%s КБ (%d%%), табличное пространство %d/%d (%-30.30s) %s/%s КБ (%d%%), табличное пространство %d/%d (%-30.30s) %s/%s КБ (%d%%), табличное пространство %d/%d (%-30.30s) %s/%s КБ (100%%), табличное пространство %d/%d %35s %s/%s КБ (100%%), табличное пространство %d/%d %35s %s/%s КБ (100%%), табличное пространство %d/%d %35s %s: поток COPY закончился до завершения последнего файла
 %s: в stdout можно вывести только одно табличное пространство, всего в СУБД их %i
 %s: нет доступа к каталогу "%s": %s
 %s: не удалось закрыть сжатый файл "%s": %s
 %s: не удалось закрыть файл "%s": %s
 %s: не удалось подключиться к серверу: %s %s: не удалось создать файл сжатого архива "%s": %s
 %s: не удалось создать каталог "%s": %s
 %s: не удалось создать файл "%s": %s
 %s: не удалось создать символическую ссылку "%s" в "%s": %s
 %s: не удалось получить поток данных COPY: %s %s: не удалось получить конечную позицию в WAL с сервера: %s %s: не удалось получить заголовок резервной копии: %s %s: не удалось инициализировать базовое резервное копирование: %s %s: не удалось разобрать режим файла
 %s: не удалось разобрать размер файла
 %s: не удалось прочитать данные COPY: %s %s: не удалось отправить команду базового резервного копирования: %s %s: не удалось установить уровень сжатия %i: %s
 %s: не удалось установить права для каталога "%s": %s
 %s: не удалось установить права доступа для файла "%s": %s
 %s: не удалось записать файл сжатого архива "%s": %s
 %s: не удалось записать файл "%s": %s
 %s: каталог "%s" существует, но он не пуст
 %s: ошибка в конце передачи: %s %s: неверный аргумент режима контрольных точек "%s", должен быть "fast" или "spread"
 %s: неверный уровень сжатия "%s"
 %s: неверный формат вывода "%s", должен быть "plain" или "tar"
 %s: неверный размер заголовка блока tar: %i
 %s: сервер не вернул конечную позицию в WAL
 %s: сервер не вернул данные
 %s: сервер не вернул стартовую точку
 %s: целевой каталог не указан
 %s: сжимать можно только резервные копии в архиве tar
 %s: нехватка памяти
 %s: получен недопустимый каталог (слишком длинное имя): %s
 %s: эта сборка программы не поддерживает сжатие
 %s: слишком много аргументов командной строки (первый: "%s")
 %s: нераспознанный индикатор связи "%c"
 %s: неподдерживаемая версия сервера (%s)
 Пароль:  Для дополнительной информации попробуйте "%s --help".
 Использование:
 