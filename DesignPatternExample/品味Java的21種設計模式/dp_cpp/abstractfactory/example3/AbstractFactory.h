#pragma once

#include "CPUApi.h"
#include "MainboardApi.h"

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
					/// <summary> * ���󹤳��Ľӿڣ��������������Ʒ����Ĳ��� </summary>
					/// 
					class AbstractFactory
					{
					///	
					///	 <summary> * ����CPU�Ķ��� </summary>
					///	 * <returns> CPU�Ķ��� </returns>
					///	 
					public:
						virtual CPUApi *createCPUApi() = 0;
					///	
					///	 <summary> * ��������Ķ��� </summary>
					///	 * <returns> ����Ķ��� </returns>
					///	 
						virtual MainboardApi *createMainboardApi() = 0;
					};
				}
			}
		}
	}
}
