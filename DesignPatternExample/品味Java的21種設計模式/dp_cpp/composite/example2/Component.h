#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example2
				{

					///
					/// <summary> * ������������Ϊ����еĶ��������ӿڣ�ʵ�ֽӿڵ�ȱʡ��Ϊ </summary>
					/// 
					class Component
					{
					///	
					///	 <summary> * ʾ�ⷽ�����������������еĹ��ܷ��� </summary>
					///	 
					public:
						virtual void someOperation() = 0;

					///	
					///	 <summary> * ����϶����м����������  </summary>
					///	 * <param name="child"> ��������϶����е�������� </param>
					///	 
						virtual void addChild(Component *child);

					///	
					///	 <summary> * ����϶������Ƴ�ĳ��������� </summary>
					///	 * <param name="child"> ���Ƴ���������� </param>
					///	 
						virtual void removeChild(Component *child);

					///	
					///	 <summary> * ����ĳ��������Ӧ��������� </summary>
					///	 * <param name="index"> ��Ҫ��ȡ����������������������0��ʼ </param>
					///	 * <returns> ������Ӧ��������� </returns>
					///	 
						virtual Component *getChildren(int index);
					};


				}
			}
		}
	}
}