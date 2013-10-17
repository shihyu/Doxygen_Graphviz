#pragma once

#include "CPUApi.h"
#include "MainboardApi.h"
#include "CPUFactory.h"
#include "MainboardFactory.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example1
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
					///	 <summary> * װ������ </summary>
					///	 * <param name="cpuType"> �ͻ�ѡ������CPU������ </param>
					///	 * <param name="mainboardType"> �ͻ�ѡ��������������� </param>
					///	 
					public:
						virtual void makeComputer(int cpuType, int mainboardType);
					///	
					///	 <summary> * ׼��װ������Ҫ����� </summary>
					///	 * <param name="cpuType"> �ͻ�ѡ������CPU������ </param>
					///	 * <param name="mainboardType"> �ͻ�ѡ��������������� </param>
					///	 
					private:
						void prepareHardwares(int cpuType, int mainboardType);
					};

				}
			}
		}
	}
}