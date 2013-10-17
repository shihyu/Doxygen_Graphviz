#pragma once

#include "CPUApi.h"

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
					/// <summary> *Intel��CPUʵ�� </summary>
					/// 
					class IntelCPU : public CPUApi
					{
					///	
					///	 <summary> * CPU�������Ŀ </summary>
					///	 
					private:
						int pins;
					///	
					///	 <summary> * ���췽��������CPU�������Ŀ </summary>
					///	 * <param name="pins"> CPU�������Ŀ </param>
					///	 
					public:
						IntelCPU(int pins);

						virtual void calculate();
					};

				}
			}
		}
	}
}