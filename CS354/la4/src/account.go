package account

import . "customer"
import "fmt"

type IAccount interface {
	Number() string
	Balance() float32
	ToString() string
	Accrue(rate float32, c chan string)
}

type Account struct {
	balance float32
	customer Customer
	number string
}

func NewAccount(number string, customer Customer, balance float32) (a *Account){
	a = new(Account)
	a.Init(number, customer, balance)
	return
}

func (a *Account) Init(number string, customer Customer, balance float32){
	a.number = number
	a.customer = customer
	a.balance = balance
}


func (a *Account) Deposit(amount float32) {
	a.balance += amount
}

func (a *Account) Withdraw(amount float32){
	a.balance -= amount
}

func (a *Account) Balance() float32 {
	return a.balance
}

func (a *Account) Number() string {
	return a.number
}

func (a *Account) Customer() string {
	return a.customer.ToString()
}

func (a *Account) ToString() string {
	return a.Number() +":"+a.Customer() + ":" + fmt.Sprintf("%f", a.Balance())+ "\n"
}