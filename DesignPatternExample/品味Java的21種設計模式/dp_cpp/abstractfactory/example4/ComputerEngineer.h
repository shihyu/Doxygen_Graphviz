#pragma once

#include "CPUApi.h"
#include "MainboardApi.h"
#include "MemoryApi.h"
#include "AbstractFactory.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example4
				{

					///
					/// <summary> * װ������ʦ���� </summary>
					/// 
					class ComputerEngineer
					{
					///	
					///	 <summary> * ������װ������Ҫ��CPU </summary>
					///	 
					private:
						CPUApi *cpu;
					///	
					///	 <summary> * ������װ������Ҫ������ </summary>
					///	 
						MainboardApi *mainboard;
					///	
					///	 <summary> * ������װ������Ҫ���ڴ� </summary>
					///	 
						MemoryApi *memory;

					///	
					///	 <summary> * װ������ </summary>
					///	 * <param name="schema"> �ͻ�ѡ���װ������ </param>
					///	 
					public:
						virtual void makeComputer(AbstractFactory *schema);
					///	
					///	 <summary> * ׼��װ������Ҫ����� </summary>
					///	 * <param name="schema"> �ͻ�ѡ���װ������ </param>
					///	 
					private:
						void prepareHardwares(AbstractFactory *schema);
					};

				}
			}
		}
	}
}