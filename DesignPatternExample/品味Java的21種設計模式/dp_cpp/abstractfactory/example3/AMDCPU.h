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
				namespace example3
				{
					///
					/// <summary> * AMD��CPUʵ�� </summary>
					/// 
					class AMDCPU : public CPUApi
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
						AMDCPU(int pins);
						virtual void calculate();
					};

				}
			}
		}
	}
}