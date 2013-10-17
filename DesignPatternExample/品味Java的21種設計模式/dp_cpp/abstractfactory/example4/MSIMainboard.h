#pragma once

#include "MainboardApi.h"

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
					/// <summary> * ΢�ǵ����� </summary>
					/// 
					class MSIMainboard : public MainboardApi
					{
					///	
					///	 <summary> * CPU��۵Ŀ��� </summary>
					///	 
					private:
						int cpuHoles;
					///	
					///	 <summary> * ���췽��������CPU��۵Ŀ��� </summary>
					///	 * <param name="cpuHoles"> CPU��۵Ŀ��� </param>
					///	 
					public:
						MSIMainboard(int cpuHoles);
						virtual void installCPU();
					};

				}
			}
		}
	}
}