"""
Mustafa Aslanov HW2
Best to view this file on Jupyter Notebook
Reason: Underline function doesn't show on cmd terminal
"""

import pandas as pd
import numpy as np     

class VM:
    def __init__(self, V,M,S,P):
        self.V = V
        self.M = M
        self.S = S
        self.P = P
        self.file = open("log.txt","w+",encoding ="utf-8")
        self.stack = []
        self.p_mem = []
        self.v_mem = []
        self.swap_memory = []
        self.swap = []
        self.initSizes()
        
        output = "Initialized\n"
        self.file.write(output)
        print(output)
        
        
    def initSizes(self):
        self.pageSize = 1000*self.P
        self.numOfPages = int(self.V/self.P) 

        self.numOfSwaps = int(self.S/self.P)
        for i in range(self.numOfSwaps):
            self.swap_memory.append(-1)
        for i in range(self.numOfSwaps):
            self.swap.append(np.zeros(self.pageSize))
        
        self.numOfFrames = int(self.M/self.P)
        for i in range(self.numOfFrames):
            self.p_mem.append(np.zeros(self.pageSize))
        for i in range(self.numOfFrames):
            self.v_mem.append(-1)
            
            
    def read(self,address):
            
            page_num = int(address/self.pageSize)
            displacement = address%self.pageSize
            if page_num in self.v_mem:
                frame_num = self.v_mem.index(page_num)
                value = self.p_mem[frame_num][displacement]
                value = int(value)
                self.update(page_num)
                    
                output = f"Value at address {address} is {value}\n"
                self.file.write('\n'+output)
                print(output)

            else:
                output = f"Page fault at {address}"
                self.file.write('\n'+output)
                print(output)           
                self.faultHandling(page_num,address)
                    
                frame_num = self.v_mem.index(page_num)
                value = self.p_mem[frame_num][displacement]
                value = int(value)
                output = f"Value at address {address} is {value}\n"
                self.file.write('\n'+output)
                print(output)
                self.update(page_num)
                            
    def write(self,address,value):   

                page_num = int(address/self.pageSize)
                displacement = address%self.pageSize             
 
                if page_num in self.v_mem:
                    frame_num = self.v_mem.index(page_num)
                    self.p_mem[frame_num][displacement] = value
                    
                    self.update(page_num,Write=1)
                    
                    output = f"Written {value} to address {address}\n"
                    self.file.write('\n'+output)
                    print(output)
                
                else: 
                    output = f"Page fault at {address}"
                    self.file.write('\n'+output)
                    print(output)           
                    self.faultHandling(page_num,address)
                    
                    frame_num = self.v_mem.index(page_num)
                    self.p_mem[frame_num][displacement] = value 
                    output = f"Written {value} to address {address}\n"
                    self.file.write('\n'+output)
                    print(output)
                    self.update(page_num,Write=True)
    
            
    def faultHandling(self,page_num,address):
        
        if -1 in self.v_mem:
            frame_num = self.v_mem.index(-1)
            self.v_mem[frame_num] = page_num
                    
            self.memLoad(page_num,frame_num) 
            
        else:
            flag = False
            while flag == False:
                flag = self.evict()
            self.faultHandling(page_num,address)
            
    
    def memLoad(self,page_num,fIndex):
        if page_num not in self.swap_memory:
            output = f"Loading page {page_num} to frame {fIndex}"
            self.file.write('\n'+output)
            output = f"Loading page {page_num} \033[4mto frame {fIndex}\033[0m"
            print(output)
            
            self.p_mem[fIndex] = np.zeros(self.pageSize)
        else: 
            sIndex = self.swap_memory.index(page_num)
            
            
            output = f"Loading_page {page_num} from swap_memory block {sIndex} to frame {fIndex}"
            self.file.write('\n'+output)
            output = f"Loading_page {page_num} \033[4mfrom swap_memory block {sIndex}\033[0m to frame {fIndex}"
            print(output)
            
            self.p_mem[fIndex] = self.swap[sIndex]
            self.swap[sIndex] = np.zeros(self.pageSize)
            self.swap_memory[sIndex] = -1
    
    def evict(self):
            if self.stack[0][1] == 1:
                temp = self.stack[0]
                self.stack.pop(0)
                temp = [temp[0],0,temp[2]]
                self.stack.append(temp)
                return False
            else:
                page = self.stack[0]
                index = self.v_mem.index(page[0])
                self.stack.pop(0)
                
                output = f"Evicting page {page[0]} from frame {index}" #due to the fact that underline is not visible in text file removing it for logs
                self.file.write('\n'+output)
                output = f"\033[4mEvicting page {page[0]} from frame {index}\033[0m"
                print(output)
                
                if page[2] == 1:
                    self.swapSaving(page[0],index)
                    
                self.v_mem[index] = -1
                self.p_mem[index] = (np.zeros(self.pageSize))
                return True

    def update(self,page_num,Write = 0):
        index = -1
        for page in self.stack:
            if page_num == page[0]:
                index = self.stack.index(page)
        if index == -1:
            self.stack.append([page_num,1,Write])
        else:
            if Write == 1:
                 self.stack[index] =[page_num,1,1]
            else:
                 self.stack[index] =[page_num,1,self.stack[index][2]]

    def swapSaving(self,page_num,fIndex):
        sIndex = self.swap_memory.index(-1)
        self.swap_memory[sIndex] = page_num
        self.swap[sIndex] = self.p_mem[fIndex]
        output = f"Saving page {page_num} to swap_memory block {sIndex}"        
        self.file.write('\n'+output)
        output = f"\033[4mSaving page {page_num} to swap_memory block {sIndex}\033[0m"
        print(output)
        
    def end(self):
         
        X = self.v_mem
        for i in range(len(X)):
            if X[i] == -1:
                X[i] = "-"
            else:
                X[i] = str(X[i])
        memory_t = ", ".join(X)
        output = f"Memory - {memory_t}."
        self.file.write('\n'+output)
        print(output)
        
        Y = self.swap_memory
        for i in range(len(Y)):
            if Y[i] == -1:
                Y[i] = "0"
            else:
                Y[i] = str(Y[i])
        memory_t = ", ".join(Y)
        output = f"Swap - {memory_t}."
        self.file.write('\n'+output)
        print(output)
        self.file.close()
    
        
def implement():
    with open("input.txt","r",encoding="utf-8") as input:
        for line in input.readlines():
            init = line.split(" ")
            if init[0] == "Init":
                V = int(init[1])
                M = int(init[2])
                S = int(init[3])
                P = int(init[4])
                memory = VM(V,M,S,P)

            elif init[0] == "Read":
                address = int(init[1])
                memory.read(address)
            elif init[0] == "Write":
                address = int(init[1])
                value = int(init[2])
                memory.write(address,value)
            elif init[0] == "Exit":
                memory.end()    

implement()