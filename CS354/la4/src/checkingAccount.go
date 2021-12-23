package account

import . "customer"

type ICheckingAccount interface {
	IAccount
	Accrue(rate float32, c chan string)
}

type CheckingAccount struct {
	Account
}

func NewCheckingAccount(number string, customer Customer, balance float32) (ca *CheckingAccount){
	ca=new(CheckingAccount)
	ca.Init(number, customer, balance)
	return
}

func (ca *CheckingAccount) Init(number string, customer Customer, balance float32){
	ca.Account.Init(number, customer, balance)
}

func (ca *CheckingAccount) Accrue(rate float32, c chan string){
	c <- ca.Account.ToString()
}