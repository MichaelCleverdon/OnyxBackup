package bank

import . "account"
import . "fmt"
type IBank interface {
	Accrue(rate float32)
	ToString() string
}

type Bank struct {
	accounts map[*IAccount]IAccount
}

func (b *Bank) Accrue(rate float32){
	channel:= make(chan string)
	for _,a:=range b.accounts{
		go a.Accrue(rate, channel)
	}
	for i:=0; i < len(b.accounts); i++{
		str := <- channel
		Printf(str)
	}
}

func (b *Bank) Add(a IAccount){
	b.accounts[&a] = a	
}

func NewBank() (b *Bank){
	b = new(Bank)
	b.Init()
	return
}

func (b *Bank) Init(){
	b.accounts = make(map[*IAccount]IAccount)
}

func (b *Bank) ToString() string{
	str := ""
	for _,a:=range b.accounts{
		str += a.ToString()
	}
	return str
}