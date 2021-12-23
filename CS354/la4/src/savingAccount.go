package account

import . "customer"
type ISavingAccount interface {
	IAccount
	Accrue(rate float32, c chan string)
}

type SavingAccount struct {
	Account
	interest float32
}

func NewSavingAccount(number string, customer Customer, balance float32) (sa *SavingAccount){
	sa=new(SavingAccount)
	sa.Init(number, customer, balance)
	return
}

func (sa *SavingAccount) Init(number string, customer Customer, balance float32){
	sa.Account.Init(number, customer, balance)
}

func (sa *SavingAccount) Accrue(rate float32, c chan string){
	  sa.interest += sa.balance*rate;
	  sa.balance += sa.balance*rate;
	  c <- sa.Account.ToString()	  
}