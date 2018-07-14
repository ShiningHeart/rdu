# compiler
CC=gcc
# executable name
OUT=dx
# non-standard include paths
#INC= -I../
INC = -I/../include

MAKEFLAGS = -k
# compiler flags
CFLAGS= -g -std=c99 -Wall -c \
		-O2 -Wno-parentheses \
		-fno-strict-aliasing \
	    -DSO_RXQ_OVFL=40 \
	    -DPF_CAN=29 \
	    -DAF_CAN=PF_CAN
# libraries
LIBS= -lrt -pthread 
# object file directory
OBJDIR=obj
# object files
OBJS= $(addprefix $(OBJDIR)/, \
	dxtask.o dxcan.o lib.o )


all: directories $(OUT)


$(OUT): $(OBJS)
	$(CC) -g -o $(OUT) $^ $(LIBS)

$(OBJDIR)/%.o: %.c
	$(CC) $(CFLAGS) $(INC) -o $@ $<


.PHONY: directories
directories:
	mkdir -p $(OBJDIR)

.PHONY: clean
clean:
	rm $(OBJDIR)/*.o $(OUT)

