package customer

type ICustomer interface {
	Name() string
	ToString() string
}

type Customer struct {
	name string
}

func NewCustomer(name string) (c *Customer) {
	c = new(Customer)
	c.Init(name)
	return
}

func (c *Customer) Init(name string){
	c.name=name
}

func (c *Customer) ToString() string {
	return c.name;
}

func (c *Customer) Name() string {return c.name}