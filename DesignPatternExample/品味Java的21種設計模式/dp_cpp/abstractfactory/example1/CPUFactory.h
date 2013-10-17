#pragma once

#include "CPUApi.h"
#include "IntelCPU.h"
#include "AMDCPU.h"

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
					/// <summary> * ����CPU�ļ򵥹��� </summary>
					/// 
					class CPUFactory
					{
					///	
					///	 <summary> * ����CPU�ӿڶ���ķ��� </summary>
					///	 * <param name="type"> ѡ��CPU���͵Ĳ��� </param>
					///	 * <returns> CPU�ӿڶ���ķ��� </returns>
					///	 
					public:
						static CPUApi *createCPUApi(int type);
					};

				}
			}
		}
	}
}