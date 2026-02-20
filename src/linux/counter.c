#include <linux/init.h> 
#include <linux/kernel.h> 
#include <linux/module.h>
#include <linux/fs.h>
#include <linux/proc_fs.h>
#include <linux/seq_file.h>
#include <linux/uaccess.h>
#include <linux/atomic.h>

/*
 * Creamos una variable atómica, garantizando que el kernel
 * lo ejecute
 * */
static atomic_t read_counter = ATOMIC_INIT(0);

// Definimos la manera de mostrar counter
static int counter_show(struct seq_file *m, void *v){
	seq_printf(m, "Modulo counter abierto.");
	return 0;
}

// Definimos la manera de abrir counter
static int counter_open(struct inode *inode, struct file *file){
	return single_open(file, counter_show, NULL);
}

// Definimos la manera de leer el archivo
static ssize_t counter_read(struct file *filp, char __user *buf, size_t count, loff_t *o){

	// Creamos un buffer temporal
	char temp_buf[128];
	
	// Si la posición es mayor a cero, significa que el archivo termino
	if(*o > 0){
		return 0;
	}
	// Incrementamos el contador y asignamos el nuevo valor
	int counter = atomic_inc_return(&read_counter);
	
	// Creamos el mensaje
	size_t len = snprintf(temp_buf, sizeof(temp_buf), "/proc/counter se ha leído %d veces.\n", counter);
	
	// Copiamos del espacio del kernel al espacio de usuario
	if(copy_to_user(buf, temp_buf, len)){
		return -EFAULT;
	}
	
	if(count > 0){
		len = count;
	}
	
	// Actualizamos la posición en el archivo
	*o = len;
	return len;
}

// Establecemos la configuración 
static const struct proc_ops counter_fops = {
	.proc_open = counter_open,
	.proc_read = counter_read,
	.proc_lseek = seq_lseek,
	.proc_release = single_release,
};

/*
 * Método counter_start 
 * */
static int __init counter_start(void){
	
	// Creamos la entrada counter en /proc y verificamos que no haya fallado la creación
	if(proc_create("counter", 0, NULL, &counter_fops) == NULL){
		return -ENOMEM;
	}
	
	return 0;
}

static void __exit counter_end(void){
	remove_proc_entry("counter", NULL);
}

module_init(counter_start);
module_exit(counter_end);

MODULE_LICENSE("GPL");
