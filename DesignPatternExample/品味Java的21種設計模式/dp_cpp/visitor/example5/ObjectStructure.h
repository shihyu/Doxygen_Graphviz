#pragma once

#include "Component.h"
#include "Visitor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example5
				{
					///
					/// <summary> * ����ṹ,ͨ���������Ԫ�ض�����б������÷������ܷ��ʵ����е�Ԫ�� </summary>
					/// 
					class ObjectStructure
					{
					///	
					///	 <summary> * ��ʾ����ṹ��������һ����Ͻṹ </summary>
					///	 
					private:
						Component *root;
					///	
					///	 <summary> * �ṩ���ͻ��˲����ĸ߲�ӿ� </summary>
					///	 * <param name="visitor"> �ͻ�����Ҫʹ�õķ����� </param>
					///	 
					public:
						virtual void handleRequest(Visitor *visitor);
					///	
					///	 <summary> * ������϶���ṹ </summary>
					///	 * <param name="ele"> ��϶���ṹ </param>
					///	 
						virtual void setRoot(Component *ele);
					};

				}
			}
		}
	}
}