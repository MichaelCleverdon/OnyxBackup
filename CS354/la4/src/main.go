package main

import . "account"
import . "customer"
import . "bank"
func main(){
	b := NewBank()
	b.Add(NewCheckingAccount("01001", *NewCustomer("Ann"), 100.00))
	b.Add(NewSavingAccount("01002", *NewCustomer("Ann"), 200.00))
	b.Accrue(.02)
}