#pragma once

#include "Abstraction.h"
#include "Implementor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example5
				{
					///
					/// <summary> * ������Abstraction����Ľӿڹ��� </summary>
					/// 
					class RefinedAbstraction : public Abstraction
					{
					public:
						RefinedAbstraction(Implementor *impl);
					///	
					///	 <summary> * ʾ��������ʵ��һ���Ĺ��ܣ����ܻ�ʹ�þ���ʵ�ֲ��ֵ�ʵ�ַ�����
					///	 * ���Ǳ���������Ŀ�����ʹ��Abstraction�ж���ķ�����
					///	 * ͨ�����ʹ��Abstraction�ж���ķ�������ɸ���Ĺ��� </summary>
					///	 
						virtual void otherOperation();
					};

				}
			}
		}
	}
}