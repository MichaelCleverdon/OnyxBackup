#!/bin/bash
gccgo -g -c customer.go
gccgo -g -c account.go savingAccount.go checkingAccount.go
gccgo -g -c bank.go
gccgo -g -c main.go
