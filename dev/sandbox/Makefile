CC=gcc
OUT=app
INC= -I/usr/include/libxml2
CFLAGS= -g -std=c99 -Wall -c
LIBS= -lrt -L/usr/lib/x86_64 -lxml2
OBJS = main.o

all: $(OUT)


$(OUT): $(OBJS)
	$(CC) -g -o $(OUT) $^ $(LIBS)

%.o: %.c
	$(CC) $(CFLAGS) $(INC) -c -o $@ $<

.PHONY: clean
clean:
	rm -f *.o $(OUT)

