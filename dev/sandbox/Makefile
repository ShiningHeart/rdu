CC=gcc
OUT=app
INC=
CFLAGS= -g -std=c99 -Wall -c
LIBS= -lrt

OBJDIR=obj
OBJS= $(addprefix $(OBJDIR)/, \
	main.o)


all: $(OUT)


$(OUT): $(OBJS)
	$(CC) -g -o $(OUT) $^ $(LIBS)

$(OBJDIR)/%.o: %.c
	$(CC) $(CFLAGS) $(INC) -o $@ $<

.PHONY: clean
clean:
	rm $(OBJDIR)/*.o $(OUT)

