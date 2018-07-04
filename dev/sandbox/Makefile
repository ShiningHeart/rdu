CC=gcc
OUT=app
INC=
CFLAGS= -g -std=c99 -Wall -c
LIBS= -lrt
OBJS = main.o

all: $(OUT)


$(OUT): $(OBJS)
	$(CC) -g -o $(OUT) $^ $(LIBS)

%.o: %.c
	$(CC) $(CFLAGS) $(INC) -c -o $@ $<

.PHONY: clean
clean:
	rm -f *.o $(OUT)

