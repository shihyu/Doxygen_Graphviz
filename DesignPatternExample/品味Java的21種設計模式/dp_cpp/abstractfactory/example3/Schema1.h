#pragma once

#include "AbstractFactory.h"
#include "CPUApi.h"
#include "IntelCPU.h"
#include "MainboardApi.h"
#include "GAMainboard.h"

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
					/// <summary> * װ������һ��Intel ��CPU + ���ε�����
					/// * ���ﴴ��CPU����������ʱ���Ƕ�Ӧ�ģ���ƥ���ϵ� </summary>
					/// 
					class Schema1 : public AbstractFactory
					{
					public:
						virtual CPUApi *createCPUApi();
						virtual MainboardApi *createMainboardApi();
					};
				}
			}
		}
	}
}