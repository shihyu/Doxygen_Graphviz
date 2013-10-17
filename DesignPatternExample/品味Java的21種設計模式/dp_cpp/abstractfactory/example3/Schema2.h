#pragma once

#include "AbstractFactory.h"
#include "CPUApi.h"
#include "AMDCPU.h"
#include "MainboardApi.h"
#include "MSIMainboard.h"

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
					/// <summary> * װ����������AMD��CPU + ΢�ǵ�����
					/// * ���ﴴ��CPU����������ʱ���Ƕ�Ӧ�ģ���ƥ���ϵ� </summary>
					/// 
					class Schema2 : public AbstractFactory
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